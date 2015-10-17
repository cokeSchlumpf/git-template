package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "name", "value" } )
@XmlAccessorType(XmlAccessType.FIELD)
public class TemplateVariable {

	@XmlElement(name = "name", required = true)
	private String name;
	
	@XmlElement(name = "value", required = true)
	private String value;

	public TemplateVariable() {
		
	}
	
	public TemplateVariable(String name, String value) {
		this();
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "TemplateVariable [name=" + name + ", value=" + value + "]";
	}
	
}
