package org.chudian.mj.dao;

public class DBParameter {
    private int position;
    public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public DBParaType getDbType() {
		return dbType;
	}

	public void setDbType(DBParaType dbType) {
		this.dbType = dbType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	private DBParaType dbType;
    @Override
	public String toString() {
		return "DBParameter [position=" + position + ", dbType=" + dbType
				+ ", value=" + value + "]";
	}

	private Object value;
    
    public DBParameter(int position,DBParaType dbType,Object value){
    	this.position = position;
    	this.dbType = dbType;
    	this.value = value;
    }
}
