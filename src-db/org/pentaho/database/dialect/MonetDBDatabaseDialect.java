package org.pentaho.database.dialect;

import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseInterface;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.row.ValueMetaInterface;

public class MonetDBDatabaseDialect extends AbstractDatabaseDialect {

    public static final IDatabaseType	DBTYPE	= 
		new DatabaseType(
				"MonetDB", 
				"MONETDB", 
				DatabaseAccessType.getList(
						DatabaseAccessType.NATIVE, 
						DatabaseAccessType.ODBC, 
						DatabaseAccessType.JNDI
					), 
				-1, 
				null
			);

	public IDatabaseType getDatabaseType() {
		return DBTYPE;
	}

	public String getNativeDriver() {
		return "nl.cwi.monetdb.jdbc.MonetDriver";
	}

	protected String getNativeJdbcPre() {
		return "jdbc:monetdb://";
	}

	/**
	 * @see DatabaseInterface#getNotFoundTK(boolean)
	 */
	public int getNotFoundTK(boolean use_autoinc)
	{
		if ( supportsAutoInc() && use_autoinc)
		{
			return 1;
		}
		return super.getNotFoundTK(use_autoinc);
	}
	
	/**
	 * Checks whether or not the command setFetchSize() is supported by the JDBC driver...
	 * @return true is setFetchSize() is supported!
	 */
	public boolean isFetchSizeSupported()
	{
		return false;
	}
	
	/**
	 * @return true if the database supports bitmap indexes
	 */
	public boolean supportsBitmapIndex()
	{
		return true;
	}
	
	public boolean supportsAutoInc() {
		return true;
	}
	
	public boolean supportsBatchUpdates() {
		return true;
	}
	
	public boolean supportsBooleanDataType() {
		return true;
	}
	
	public boolean supportsSetMaxRows() {
		return true;
	}
	
	/**
	 * @return true if Kettle can create a repository on this type of database.
	 */
	public boolean supportsRepository()
	{
		return false;
	}
	
	/**
	 * @param tableName The table to be truncated.
	 * @return The SQL statement to truncate a table: remove all rows from it without a transaction
	 */
	public String getTruncateTableStatement(String tableName)
	{
	    return "DELETE FROM "+tableName;
	}


	/**
	 * Generates the SQL statement to add a column to the specified table
	 * For this generic type, i set it to the most common possibility.
	 * 
	 * @param tablename The table to add
	 * @param v The column defined as a value
	 * @param tk the name of the technical key field
	 * @param use_autoinc whether or not this field uses auto increment
	 * @param pk the name of the primary key field
	 * @param semicolon whether or not to add a semi-colon behind the statement.
	 * @return the SQL statement to add a column to the specified table
	 */
	public String getAddColumnStatement(String tablename, ValueMetaInterface v, String tk, boolean use_autoinc, String pk, boolean semicolon)
	{
		return "ALTER TABLE "+tablename+" ADD "+getFieldDefinition(v, tk, pk, use_autoinc, true, false);
	}

	/**
	 * Generates the SQL statement to modify a column in the specified table
	 * @param tablename The table to add
	 * @param v The column defined as a value
	 * @param tk the name of the technical key field
	 * @param use_autoinc whether or not this field uses auto increment
	 * @param pk the name of the primary key field
	 * @param semicolon whether or not to add a semi-colon behind the statement.
	 * @return the SQL statement to modify a column in the specified table
	 */
	public String getModifyColumnStatement(String tablename, ValueMetaInterface v, String tk, boolean use_autoinc, String pk, boolean semicolon)
	{
		return "ALTER TABLE "+tablename+" MODIFY "+getFieldDefinition(v, tk, pk, use_autoinc, true, false);
	}

	public String getFieldDefinition(ValueMetaInterface v, String tk, String pk, boolean use_autoinc, boolean add_fieldname, boolean add_cr)
	{
		String retval="";
		
		String fieldname = v.getName();
		int    length    = v.getLength();
		int    precision = v.getPrecision();
		
		if (add_fieldname) retval+=fieldname+" ";
		
		int type         = v.getType();
		switch(type)
		{
		case ValueMetaInterface.TYPE_DATE   : retval+="TIMESTAMP"; break;
		case ValueMetaInterface.TYPE_BOOLEAN:
			if (supportsBooleanDataType()) {
				retval+="BOOLEAN"; 
			} else {
				retval+="CHAR(1)";
			}
			break;
		case ValueMetaInterface.TYPE_NUMBER    :
		case ValueMetaInterface.TYPE_INTEGER   : 
        case ValueMetaInterface.TYPE_BIGNUMBER : 
			if (fieldname.equalsIgnoreCase(tk) || // Technical key
			    fieldname.equalsIgnoreCase(pk)    // Primary key
			    ) 
			{
				if (use_autoinc)
				{
					retval+="SERIAL";
				}
				else
				{
					retval+="BIGINT";
				}
			} 
			else
			{
				// Integer values...
				if (precision==0)
				{
					if (length>9)
					{
						if (length<19) {
							// can hold signed values between -9223372036854775808 and 9223372036854775807
							// 18 significant digits
							retval+="BIGINT";
						} else {
							retval+="DECIMAL("+length+")";
						}
					}
					else
					{
						retval+="INT";
					}
				}
				// Floating point values...
				else  
				{
					if (length>15)
					{
						retval+="DECIMAL("+length;
						if (precision>0) retval+=", "+precision;
						retval+=")";
					}
					else
					{
						// A double-precision floating-point number is accurate to approximately 15 decimal places.
						// http://mysql.mirrors-r-us.net/doc/refman/5.1/en/numeric-type-overview.html 
						retval+="DOUBLE";
					}
				}
			}
			break;
		case ValueMetaInterface.TYPE_STRING:
			if (length>=DatabaseMeta.CLOB_LENGTH)
			{
				retval+="CLOB";
			}
			else
			{
				retval+="VARCHAR"; 
				if (length>0)
				{
					retval+="("+length;
				}
				else
				{
					retval+="("; // Maybe use some default DB String length?
				}
				retval+=")";
			}
			break;
		default:
			retval+=" UNKNOWN";
			break;
		}
		
		if (add_cr) retval+=Const.CR;
		
		return retval;
	}

    public String[] getUsedLibraries()
    {
        return new String[] { "monetdb-1.7-jdbc.jar", };
    }
}
