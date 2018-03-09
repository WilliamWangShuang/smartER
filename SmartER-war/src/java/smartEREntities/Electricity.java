package smartEREntities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author William
 */
@Entity
@Table(name = "ELECTRICITY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Electricity.findAll", query = "SELECT e FROM Electricity e")
    , @NamedQuery(name = "Electricity.findByUsageid", query = "SELECT e FROM Electricity e WHERE e.usageid = :usageid")
    , @NamedQuery(name = "Electricity.findByUsagedate", query = "SELECT e FROM Electricity e WHERE e.usagedate = :usagedate")
    , @NamedQuery(name = "Electricity.findByUsagehour", query = "SELECT e FROM Electricity e WHERE e.usagehour = :usagehour")
    , @NamedQuery(name = "Electricity.findByFridgeusage", query = "SELECT e FROM Electricity e WHERE e.fridgeusage = :fridgeusage")
    , @NamedQuery(name = "Electricity.findByAcusage", query = "SELECT e FROM Electricity e WHERE e.acusage = :acusage")
    , @NamedQuery(name = "Electricity.findByWmusage", query = "SELECT e FROM Electricity e WHERE e.wmusage = :wmusage")
    , @NamedQuery(name = "Electricity.findByTemperature", query = "SELECT e FROM Electricity e WHERE e.temperature = :temperature")})
public class Electricity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "USAGEID")
    private Integer usageid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USAGEDATE")
    @Temporal(TemporalType.DATE)
    private Date usagedate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USAGEHOUR")
    private int usagehour;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "FRIDGEUSAGE")
    private BigDecimal fridgeusage;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACUSAGE")
    private BigDecimal acusage;
    @Basic(optional = false)
    @NotNull
    @Column(name = "WMUSAGE")
    private BigDecimal wmusage;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TEMPERATURE")
    private int temperature;
    @JoinColumn(name = "RESID", referencedColumnName = "RESID")
    @ManyToOne(optional = false)
    private Resident resid;

    public Electricity() {
    }

    public Electricity(Integer usageid) {
        this.usageid = usageid;
    }

    public Electricity(Integer usageid, Date usagedate, int usagehour, BigDecimal fridgeusage, BigDecimal acusage, BigDecimal wmusage, int temperature) {
        this.usageid = usageid;
        this.usagedate = usagedate;
        this.usagehour = usagehour;
        this.fridgeusage = fridgeusage;
        this.acusage = acusage;
        this.wmusage = wmusage;
        this.temperature = temperature;
    }

    public Integer getUsageid() {
        return usageid;
    }

    public void setUsageid(Integer usageid) {
        this.usageid = usageid;
    }

    public Date getUsagedate() {
        return usagedate;
    }

    public void setUsagedate(Date usagedate) {
        this.usagedate = usagedate;
    }

    public int getUsagehour() {
        return usagehour;
    }

    public void setUsagehour(int usagehour) {
        this.usagehour = usagehour;
    }

    public BigDecimal getFridgeusage() {
        return fridgeusage;
    }

    public void setFridgeusage(BigDecimal fridgeusage) {
        this.fridgeusage = fridgeusage;
    }

    public BigDecimal getAcusage() {
        return acusage;
    }

    public void setAcusage(BigDecimal acusage) {
        this.acusage = acusage;
    }

    public BigDecimal getWmusage() {
        return wmusage;
    }

    public void setWmusage(BigDecimal wmusage) {
        this.wmusage = wmusage;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public Resident getResid() {
        return resid;
    }

    public void setResid(Resident resid) {
        this.resid = resid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usageid != null ? usageid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Electricity)) {
            return false;
        }
        Electricity other = (Electricity) object;
        if ((this.usageid == null && other.usageid != null) || (this.usageid != null && !this.usageid.equals(other.usageid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "smartEREntities.Electricity[ usageid=" + usageid + " ]";
    }
    
}
