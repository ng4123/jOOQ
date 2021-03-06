/**
 * This class is generated by jOOQ
 */
package org.jooq.test.postgres.generatedclasses.tables;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TUnsigned extends org.jooq.impl.TableImpl<org.jooq.test.postgres.generatedclasses.tables.records.TUnsignedRecord> {

	private static final long serialVersionUID = -612500482;

	/**
	 * The singleton instance of <code>public.t_unsigned</code>
	 */
	public static final org.jooq.test.postgres.generatedclasses.tables.TUnsigned T_UNSIGNED = new org.jooq.test.postgres.generatedclasses.tables.TUnsigned();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.test.postgres.generatedclasses.tables.records.TUnsignedRecord> getRecordType() {
		return org.jooq.test.postgres.generatedclasses.tables.records.TUnsignedRecord.class;
	}

	/**
	 * The column <code>public.t_unsigned.u_byte</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.postgres.generatedclasses.tables.records.TUnsignedRecord, org.jooq.types.UByte> U_BYTE = createField("u_byte", org.jooq.impl.SQLDataType.TINYINTUNSIGNED, this, "");

	/**
	 * The column <code>public.t_unsigned.u_short</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.postgres.generatedclasses.tables.records.TUnsignedRecord, org.jooq.types.UShort> U_SHORT = createField("u_short", org.jooq.impl.SQLDataType.SMALLINTUNSIGNED, this, "");

	/**
	 * The column <code>public.t_unsigned.u_int</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.postgres.generatedclasses.tables.records.TUnsignedRecord, org.jooq.types.UInteger> U_INT = createField("u_int", org.jooq.impl.SQLDataType.INTEGERUNSIGNED, this, "");

	/**
	 * The column <code>public.t_unsigned.u_long</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.postgres.generatedclasses.tables.records.TUnsignedRecord, org.jooq.types.ULong> U_LONG = createField("u_long", org.jooq.impl.SQLDataType.BIGINTUNSIGNED, this, "");

	/**
	 * Create a <code>public.t_unsigned</code> table reference
	 */
	public TUnsigned() {
		this("t_unsigned", null);
	}

	/**
	 * Create an aliased <code>public.t_unsigned</code> table reference
	 */
	public TUnsigned(java.lang.String alias) {
		this(alias, org.jooq.test.postgres.generatedclasses.tables.TUnsigned.T_UNSIGNED);
	}

	private TUnsigned(java.lang.String alias, org.jooq.Table<org.jooq.test.postgres.generatedclasses.tables.records.TUnsignedRecord> aliased) {
		this(alias, aliased, null);
	}

	private TUnsigned(java.lang.String alias, org.jooq.Table<org.jooq.test.postgres.generatedclasses.tables.records.TUnsignedRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.test.postgres.generatedclasses.Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.postgres.generatedclasses.tables.TUnsigned as(java.lang.String alias) {
		return new org.jooq.test.postgres.generatedclasses.tables.TUnsigned(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.test.postgres.generatedclasses.tables.TUnsigned rename(java.lang.String name) {
		return new org.jooq.test.postgres.generatedclasses.tables.TUnsigned(name, null);
	}
}
