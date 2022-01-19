package test;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.test.catalog.MockCatalogReader;

public class PopulatedMockCatalogReader extends MockCatalogReader {

    public PopulatedMockCatalogReader(RelDataTypeFactory typeFactory, boolean caseSensitive) {
        super(typeFactory, caseSensitive);
    }

    private RelDataType sqlType(SqlTypeName typeName, int... args) {
        assert args.length < 3 : "unknown size of additional int args";
        return args.length == 2 ? typeFactory.createSqlType(typeName, args[0], args[1])
                : args.length == 1 ? typeFactory.createSqlType(typeName, args[0])
                : typeFactory.createSqlType(typeName);
    }

    private RelDataType nullable(RelDataType type) {
        return typeFactory.createTypeWithNullability(type, true);
    }

    // These and the methods above, lifted from org.apache.calcite.test.catalog.Fixture
    final RelDataType intType = sqlType(SqlTypeName.INTEGER);
    final RelDataType varchar20Type = sqlType(SqlTypeName.VARCHAR, 20);
    final RelDataType varchar10Type = sqlType(SqlTypeName.VARCHAR, 10);
    final RelDataType intTypeNull = nullable(intType);
    final RelDataType timestampType = sqlType(SqlTypeName.TIMESTAMP);
    final RelDataType booleanType = sqlType(SqlTypeName.BOOLEAN);
    final RelDataType dateType = sqlType(SqlTypeName.DATE);

    @Override
    public MockCatalogReader init() {

        // Register "SALES" schema.
        MockSchema salesSchema = new MockSchema("SALES");
        registerSchema(salesSchema);

        // Register "EMP" table.
        final MockTable empTable =
                MockTable.create(this, salesSchema, "EMP", false, 14);
        empTable.addColumn("EMPNO", intType, true);
        empTable.addColumn("ENAME", varchar20Type);
        empTable.addColumn("JOB", varchar10Type);
        empTable.addColumn("MGR", intTypeNull);
        empTable.addColumn("HIREDATE", timestampType);
        empTable.addColumn("SAL", intType);
        empTable.addColumn("COMM", intType);
        empTable.addColumn("DEPTNO", intType);
        empTable.addColumn("SLACKER", booleanType);
        registerTable(empTable);

        return this;

    }
}
