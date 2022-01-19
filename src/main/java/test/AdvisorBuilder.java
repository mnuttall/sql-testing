package test;

import org.apache.calcite.config.Lex;
import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.sql.advise.SqlAdvisor;
import org.apache.calcite.sql.advise.SqlAdvisorValidator;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.sql.validate.SqlValidatorWithHints;
import org.apache.flink.sql.parser.impl.FlinkSqlParserImpl;

public class AdvisorBuilder {

    SqlAdvisor sqlAdvisor;

    public SqlAdvisor getSqlAdvisor () {
        return sqlAdvisor;
    }

    public AdvisorBuilder() {

        org.apache.calcite.sql.validate.SqlValidator.Config config = org.apache.calcite.sql.validate.SqlValidator.Config.DEFAULT
                .withSqlConformance(SqlConformanceEnum.DEFAULT)
                .withTypeCoercionEnabled(true)
                .withLenientOperatorLookup(false);

        RelDataTypeSystem typeSystem = RelDataTypeSystem.DEFAULT;
        RelDataTypeFactory typeFactory = new JavaTypeFactoryImpl((RelDataTypeSystem) typeSystem);
        SqlValidatorWithHints sqlValidatorWithHints = new SqlAdvisorValidator(
                SqlStdOperatorTable.instance(),
                new PopulatedMockCatalogReader(typeFactory, true).init(),
                (RelDataTypeFactory) typeFactory,
                config);

        SqlParser.Config parserConfig = SqlParser.config()
                .withParserFactory(FlinkSqlParserImpl.FACTORY)
                .withLex(Lex.JAVA)
                .withIdentifierMaxLength(256);

        sqlAdvisor = new SqlAdvisor(sqlValidatorWithHints, parserConfig);
    }
}
