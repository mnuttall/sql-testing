package test;

import org.apache.calcite.sql.advise.SqlAdvisor;
import org.apache.calcite.sql.parser.StringAndPos;
import org.apache.calcite.sql.validate.SqlMoniker;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class TestCompletion {

    static SqlAdvisor sqlAdvisor;

    @BeforeClass
    public static void init() {
        AdvisorBuilder c = new AdvisorBuilder();
        sqlAdvisor = c.getSqlAdvisor();
    }

    @Test
    public void completionForTableNames() {
        String sql = "select a.mgr from ^stuff a";
        StringAndPos sap = StringAndPos.of(sql);

        List<SqlMoniker> results = sqlAdvisor.getCompletionHints(
                sap.sql,
                sap.pos);

        for (SqlMoniker s : results) {
            System.out.println("--- " + s.toString());
        }
    }

    @Test
    public void completionForColumnNames() {
        String sql = "select a.^ from sales.emp a";
        StringAndPos sap = StringAndPos.of(sql);

        List<SqlMoniker> results = sqlAdvisor.getCompletionHints(
                sap.sql,
                sap.pos);

        for (SqlMoniker s : results) {
            System.out.println("--> " + s.toString());
        }
    }
}
