package org.dorkmaster.pipeline.serial;

import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.Stage;
import org.dorkmaster.pipeline.exception.UnresolvedDependenciesException;
import org.dorkmaster.util.TimerMessage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DependencyResolver {
    public static class Node {
        protected Set<String> provides = new HashSet<>();
        protected Collection<Stage> stages = new CopyOnWriteArrayList<>();

        protected Node parent;
        protected Node next;

        public Node() {
        }

        public Node(Node parent) {
            this.parent = parent;
            this.parent.next = this;
        }

        public Collection<String> findProvidedDependencies() {
            Collection<String> result = new HashSet<>();

            // we don't care about the current node
            Node tmp = this.parent;
            while (tmp != null) {
                result.addAll(tmp.provides);
                tmp = tmp.parent;
            }

            return result;
        }

        public void addStages(PipelineContext ctx, Collection<Stage> incomingStages) {
            if (incomingStages.isEmpty()) {
                return;
            }

            Collection<String> nodeProvides = new HashSet<>(findProvidedDependencies());
            nodeProvides.addAll(ctx.keys());
            Map<Stage,Stage> unresolved = new ConcurrentHashMap<>();

            incomingStages.parallelStream().forEach(a -> {
                // incoming required is likely going to be immutable
                Set<String> tmp = new HashSet<String>(a.required());
                tmp.removeAll(nodeProvides);
                if (tmp.isEmpty()) {
                    stages.add(a);
                    this.provides.addAll(a.provides());
                } else {
                    unresolved.put(a,a);
                }
            });

            if (unresolved.size() == incomingStages.size()) {
                throw new UnresolvedDependenciesException("Unresolvable dependencies");
            } else {
                if (!incomingStages.isEmpty()) {
                    new Node(this).addStages(ctx, unresolved.keySet());
                }
            }
        }
    }

    Node root = new Node();

    public DependencyResolver orderStages(PipelineContext ctx, Collection<Stage> stages) {
        root.addStages(ctx, new HashSet<Stage>(stages));
        return this;
    }

    public Collection<Collection<Stage>> getResolvedStages() {
        Collection<Collection<Stage>> result = new LinkedList<>();
        Node tmp = root;
        while (tmp != null) {
            if (tmp.stages.size() > 0) {
                result.add(tmp.stages);
            }
            tmp = tmp.next;
        }
        return result;
    }
}
