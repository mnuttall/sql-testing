package test;

import org.apache.calcite.sql.advise.SqlAdvisor;
import org.apache.calcite.sql.parser.StringAndPos;
import org.apache.calcite.sql.validate.SqlMoniker;
import org.apache.calcite.sql.validate.SqlMonikerType;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestCompletion {

    // https://issues.apache.org/jira/browse/CALCITE-2439
    // This open issue from 2018 looks relevant - I'm not sure if it means that completing `SELECT a.^ from x.y a`
    // is not possible, or just limited to certain constructions. 

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
        String sql = "select a.^ from catalog.sales.emp a";
        StringAndPos sap = StringAndPos.of(sql);

        List<SqlMoniker> results = sqlAdvisor.getCompletionHints(
                sap.sql,
                sap.pos);

        for (SqlMoniker s : results) {
            System.out.println("--> " + s.toString());
        }
    }

    @Test
    public void tryOtherCompletionMethod() {
        String sql = "select a.^ from emp a";
        StringAndPos sap = StringAndPos.of(sql);
        String replaced[] = new String[]{"one", "two", "three"};

        List<SqlMoniker> results = sqlAdvisor.getCompletionHints(sql, sap.cursor, replaced);
        System.out.println ("Replaced = " + Arrays.toString(replaced));
        int keywords = 0;
        for (SqlMoniker s : results) {
            if (s.getType() == SqlMonikerType.KEYWORD) {
                keywords++;
            } else {
                System.out.println(s.toString() + " type=" + s.getType().toString());
            }
        }
        System.out.println ("Total keywords = " + keywords);

    }
}
