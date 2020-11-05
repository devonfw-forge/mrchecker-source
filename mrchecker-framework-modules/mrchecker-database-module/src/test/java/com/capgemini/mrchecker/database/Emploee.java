package com.capgemini.mrchecker.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Emploee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int		id;
	private String	name;
	private String	surname;
	private int		salary;
	
	public Emploee(int id, String name, String surname, int salary) {
		super();
		this.setId(id);
		this.setName(name);
		this.setSurname(surname);
		this.setSalary(salary);
	}
	
	public Emploee() {
		super();
	}
	
	@Override
	public String toString() {
		return "Emploee [id=" + getId() + ", name=" + getName() + ", surname=" + getSurname() + ", salary=" + getSalary() + "]";
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
}
