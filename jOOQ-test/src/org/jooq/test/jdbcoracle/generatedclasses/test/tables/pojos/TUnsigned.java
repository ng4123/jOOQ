/**
 * This class is generated by jOOQ
 */
package org.jooq.test.jdbcoracle.generatedclasses.test.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TUnsigned implements org.jooq.test.jdbcoracle.generatedclasses.test.tables.interfaces.ITUnsigned {

	private static final long serialVersionUID = -1369677075;

	private java.math.BigDecimal uByte;
	private java.math.BigDecimal uShort;
	private java.math.BigDecimal uInt;
	private java.math.BigDecimal uLong;

	@Override
	public java.math.BigDecimal getUByte() {
		return this.uByte;
	}

	@Override
	public void setUByte(java.math.BigDecimal uByte) {
		this.uByte = uByte;
	}

	@Override
	public java.math.BigDecimal getUShort() {
		return this.uShort;
	}

	@Override
	public void setUShort(java.math.BigDecimal uShort) {
		this.uShort = uShort;
	}

	@Override
	public java.math.BigDecimal getUInt() {
		return this.uInt;
	}

	@Override
	public void setUInt(java.math.BigDecimal uInt) {
		this.uInt = uInt;
	}

	@Override
	public java.math.BigDecimal getULong() {
		return this.uLong;
	}

	@Override
	public void setULong(java.math.BigDecimal uLong) {
		this.uLong = uLong;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(org.jooq.test.jdbcoracle.generatedclasses.test.tables.interfaces.ITUnsigned from) {
		setUByte(from.getUByte());
		setUShort(from.getUShort());
		setUInt(from.getUInt());
		setULong(from.getULong());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends org.jooq.test.jdbcoracle.generatedclasses.test.tables.interfaces.ITUnsigned> E into(E into) {
		into.from(this);
		return into;
	}
}