package asia.ncc.estimation.tool.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "company")
    private String company;

    @Column(name = "tel")
    private String tel;

    @Column(name = "email")
    private String email;

    @Column(name = "billaddress")
    private String billaddress;

    @Column(name = "skypeid")
    private String skypeid;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Customer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public Customer country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompany() {
        return company;
    }

    public Customer company(String company) {
        this.company = company;
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTel() {
        return tel;
    }

    public Customer tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public Customer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBilladdress() {
        return billaddress;
    }

    public Customer billaddress(String billaddress) {
        this.billaddress = billaddress;
        return this;
    }

    public void setBilladdress(String billaddress) {
        this.billaddress = billaddress;
    }

    public String getSkypeid() {
        return skypeid;
    }

    public Customer skypeid(String skypeid) {
        this.skypeid = skypeid;
        return this;
    }

    public void setSkypeid(String skypeid) {
        this.skypeid = skypeid;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        if (customer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", country='" + getCountry() + "'" +
            ", company='" + getCompany() + "'" +
            ", tel='" + getTel() + "'" +
            ", email='" + getEmail() + "'" +
            ", billaddress='" + getBilladdress() + "'" +
            ", skypeid='" + getSkypeid() + "'" +
            "}";
    }
}
