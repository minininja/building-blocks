package org.dorkmaster.pipeline.serial;

import org.dorkmaster.pipeline.PipelineContext;
import org.dorkmaster.pipeline.Stage;
import org.dorkmaster.pipeline.exception.UnresolvedDependenciesException;

import java.util.*;

public class DependencyResolver {
    public static class Node {
        protected Set<String> provides = new HashSet<>();
        protected Collection<Stage> stages = new LinkedList<>();

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

            int preCnt = incomingStages.size();
            Collection<String> nodeProvides = new HashSet<>(findProvidedDependencies());
            nodeProvides.addAll(ctx.keys());

            incomingStages.stream().filter(a-> {
                // find everything which can be satisfied by the current node
                if (a.required().isEmpty()) {
                    return true;
                } else {
                    for (String r: a.required()) {
                        if (!nodeProvides.contains(r)) {
                            return false;
                        }
                    }
                    return true;
                }
            }).forEach( a -> {
                // go ahead and add it now
                stages.add(a);
                this.provides.addAll(a.provides());
            });
            incomingStages.removeAll(stages);

            if (incomingStages.size() == preCnt) {
                throw new UnresolvedDependenciesException("Unresolvable dependencies");
            } else {
                new Node(this).addStages(ctx, incomingStages);
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
        while (tmp != null){
            if (tmp.stages.size() > 0) {
                result.add(tmp.stages);
            }
            tmp = tmp.next;
        }
        return result;
    }
}
