package com.capgemini.mrchecker.database.integration.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;

@Entity
@Table
public class Employee {
	
	@Id
	private int		id;
	private String	name;
	private String	surname;
	private int		salary;
	
	public Employee(int id, String name, String surname, int salary) {
		super();
		this.setId(id);
		this.setName(name);
		this.setSurname(surname);
		this.setSalary(salary);
	}
	
	public Employee() {
	}
	
	@Override
	public String toString() {
		return "Employee [id=" + getId() + ", name=" + getName() + ", surname=" + getSurname() + ", salary=" + getSalary() + "]";
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public int getSalary() {
		return salary;
	}
	
	public void setSalary(int salary) {
		this.salary = salary;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (other.getClass() != getClass()) {
			return false;
		}
		
		Employee castOther = (Employee) other;
		return new EqualsBuilder()
				.append(id, castOther.id)
				.append(name, castOther.name)
				.append(surname, castOther.surname)
				.append(salary, castOther.salary)
				.isEquals();
	}
}
