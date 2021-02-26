import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
import static org.apache.tinkerpop.gremlin.structure.io.IoCore.graphml;

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

    //Visit methods
    public CymVertex visitTranslationUnit(CParser.TranslationUnitContext ctx) {
        String methodName = new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName();
        System.out.println("method name "+methodName);
        String childName = ctx.getChild(0).getClass().getSimpleName();
        System.out.println("child name" + childName);
        return visitChildren(ctx);
    }

    public CymVertex visitStatement(CParser.StatementContext ctx) {
        return visitChildren(ctx); }

    public CymVertex visitFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
        return visitChildren(ctx);
    }

    public CymVertex visitAssignmentExpression(CParser.AssignmentExpressionContext ctx) {
        return visitChildren(ctx);

    }

    public CymVertex visitPointer(CParser.PointerContext ctx) {
        return visitChildren(ctx);
    }

    public CymVertex visitTerminal(TerminalNode node) {

        System.out.println("Terminal node: "+node.getSymbol().getText());

        CymVertex terminalNode = new CymVertex();
        terminalNode.thisNode = g.addV("terminalNode")
                .property("symbol", node.getSymbol().getText())
                .next();


        return terminalNode;
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
        ParseTree tree = parser.compilationUnit();

        CTranspilerVisitor myTestVisitor = new CTranspilerVisitor(g);
        myTestVisitor.visit(tree);

        //g.io("transpiler.graph.kryo").write().iterate();
        //graph.io(Io.gryo()).writeGraph("tinkerpop-modern.kryo");
        graph.io(graphml()).writeGraph("transpiler.xml");
        graph.close();



    }
}
