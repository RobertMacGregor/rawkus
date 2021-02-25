import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;

public class CymVertex {
    public Vertex thisNode;
    public ArrayList<CymVertex> children = new ArrayList<CymVertex>();

    public CymVertex(){}

    public CymVertex(Vertex thisNode) {
        this.thisNode = thisNode;
    }

    public String toString(){
        return String.format("CymVertex: vertex: %s, children: %d\n", thisNode.property("symbol"), children.size());
    }

    public void addChild(CymVertex child){
        children.add(child);
    }

    public void addChildren(CymVertex cymVertex){
        children.addAll(cymVertex.children);
    }

    public Vertex getChildNodeAsVertex(int indexIntoArrayListofChildren){
        CymVertex child = this.children.get(indexIntoArrayListofChildren);
        return child.thisNode;
    }

    public ArrayList<Vertex> getChildrenAsVertices(){
        ArrayList<Vertex> childrenAsVertex = new ArrayList<Vertex>();
        this.children.forEach(child -> childrenAsVertex.add(child.thisNode));

        return childrenAsVertex;
    }

    public CymVertex[] vertexAsArray(){
        ArrayList<CymVertex> myArray = new ArrayList<CymVertex>();
        myArray.add(new CymVertex(thisNode));

        return (CymVertex[]) myArray.toArray();
    }
}
