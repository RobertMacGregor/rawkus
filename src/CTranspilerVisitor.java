import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

public class CTranspilerVisitor extends CBaseVisitor<CymVertex>{
    GraphTraversalSource g;

    CTranspilerVisitor(GraphTraversalSource newGTS) {
        g = newGTS;
    }

    protected CymVertex defaultResult() {
        return new CymVertex();
    }

    protected CymVertex aggregateResult( CymVertex aggregate, CymVertex nextResult) {
        //System.out.println("aggregateResult "+nextResult);
        aggregate.addChild(nextResult);
        //return nextResult;
        return aggregate;
    }

    public static void main(String[] args) throws Exception {

        TinkerGraph graph = TinkerGraph.open();
        org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource  g = traversal().withEmbedded(graph);


        InputStream is = System.in;
        is = new FileInputStream(args[0]);
        ANTLRInputStream input = new ANTLRInputStream(is);
        CLexer lexer = new CLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CParser parser = new CParser(tokens);
        ParseTree tree = parser.primaryExpression();

        CTranspilerVisitor myTestVisitor = new CTranspilerVisitor(g);
        myTestVisitor.visit(tree);

        g.io("transpiler.graph.kryo").write().iterate();
        graph.close();



    }
}
