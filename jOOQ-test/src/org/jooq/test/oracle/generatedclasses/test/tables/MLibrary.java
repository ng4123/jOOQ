/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle.generatedclasses.test.tables;

/**
 * This class is generated by jOOQ.
 *
 * snapshot table for snapshot TEST.M_LIBRARY
 */
@java.lang.SuppressWarnings("all")
public class MLibrary extends org.jooq.impl.TableImpl<org.jooq.test.oracle.generatedclasses.test.tables.records.MLibraryRecord> {

	private static final long serialVersionUID = -1657294145;

	/**
	 * The singleton instance of TEST.M_LIBRARY
	 */
	public static final org.jooq.test.oracle.generatedclasses.test.tables.MLibrary M_LIBRARY = new org.jooq.test.oracle.generatedclasses.test.tables.MLibrary();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.test.oracle.generatedclasses.test.tables.records.MLibraryRecord> getRecordType() {
		return org.jooq.test.oracle.generatedclasses.test.tables.records.MLibraryRecord.class;
	}

	/**
	 * The table column <code>TEST.M_LIBRARY.AUTHOR</code>
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.MLibraryRecord, java.lang.String> AUTHOR = createField("AUTHOR", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * The table column <code>TEST.M_LIBRARY.TITLE</code>
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.MLibraryRecord, java.lang.String> TITLE = createField("TITLE", org.jooq.impl.SQLDataType.VARCHAR, this);

	public MLibrary() {
		super("M_LIBRARY", org.jooq.test.oracle.generatedclasses.test.Test.TEST);
	}

	public MLibrary(java.lang.String alias) {
		super(alias, org.jooq.test.oracle.generatedclasses.test.Test.TEST, org.jooq.test.oracle.generatedclasses.test.tables.MLibrary.M_LIBRARY);
	}

	@Override
	public org.jooq.test.oracle.generatedclasses.test.tables.MLibrary as(java.lang.String alias) {
		return new org.jooq.test.oracle.generatedclasses.test.tables.MLibrary(alias);
	}
}