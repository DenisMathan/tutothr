package tutothr.model;

import tutothr.utils.GenderEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="student")
public class Student extends User{

	private static final long serialVersionUID = 1L;

	@Id
	Long id;

	@NotBlank(message = "Name is mandatory")
	private String name;

	@Enumerated(EnumType.STRING)
	private GenderEnum gender;


	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "course_id", referencedColumnName = "id")
	private Course course;


	public Student() {

		Address address= new Address();
		this.setAddress(address);
		this.setId((long) -1);

		Course course = new Course();
		this.setCourse(course);

	}


	public Address getAddress() {
		return address;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public void setAddress(Address address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public GenderEnum getGender() {
		return gender;
	}
	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}


}