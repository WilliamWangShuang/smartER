package smartEREntities.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import smartER.DAL.Constant;
import smartER.DAL.SmartERTools;
import smartEREntities.Electricity;
import smartEREntities.HourlyUsageInfo;
import smartEREntities.Resident;

/**
 *
 * @author William
 */
@Stateless
@Path("smarterentities.electricity")
public class ElectricityFacadeREST extends AbstractFacade<Electricity> {

    @PersistenceContext(unitName = "SmartER-warPU")
    private EntityManager em;

    public ElectricityFacadeREST() {
        super(Electricity.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Electricity entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Electricity entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Electricity find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Electricity> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Electricity> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("findByUsageDT/{usageDT}") 
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Electricity> findByUsageDT(@PathParam("usageDT") String usageDT) throws Exception{ 
        List<Electricity> result = new ArrayList<Electricity>();
        try{
            Query query = em.createNamedQuery(Electricity.GET_BY_USAGE_DATE);
            Date paramDate=new SimpleDateFormat(Constant.DATE_FORMAT).parse(usageDT);
            query.setParameter("usagedate", paramDate);
            result.addAll(query.getResultList());
        } catch (Exception ex) {
            throw ex;
        }
        return result;
    }
    
    @GET
    @Path("findByUsageHour/{hour}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Electricity> findByUsageHour(@PathParam("hour") Integer hour){          
        Query query = em.createNamedQuery(Electricity.GET_BY_USAGE_HOUR);
        query.setParameter("usagehour", hour);
        List<Electricity> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("findByFridgeUsage/{fusage}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Electricity> findByFridgeUsage(@PathParam("fusage") double fusage){          
        Query query = em.createNamedQuery(Electricity.GET_BY_FRIDGE_USAGE);
        query.setParameter("fridgeusage", fusage);
        List<Electricity> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("findByACUsage/{acusage}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Electricity> findByACUsage(@PathParam("acusage") double acusage){          
        Query query = em.createNamedQuery(Electricity.GET_BY_AC_USAGE);
        query.setParameter("acusage", acusage);
        List<Electricity> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("findByWMUsage/{wmusage}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Electricity> findByWMUsage(@PathParam("wmusage") double wmusage){          
        Query query = em.createNamedQuery(Electricity.GET_BY_WM_USAGE);
        query.setParameter("wmusage", wmusage);
        List<Electricity> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("findByTemperature/{temperature}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Electricity> findByTemperature(@PathParam("temperature") Integer temperature){          
        Query query = em.createNamedQuery(Electricity.GET_BY_TEMPERATURE);
        query.setParameter("temperature", temperature);
        List<Electricity> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("findByResId/{resid}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Electricity> findByResId(@PathParam("resid") Integer resid){ 
        Resident resident = new Resident();
        resident = em.find(resident.getClass(), resid);
        Query query = em.createNamedQuery(Electricity.GET_BY_RESID);
        query.setParameter("resId", resident);
        List<Electricity> result = query.getResultList();
        return result;
    }
    
    @GET     
    @Path("findByResidentFullName/{firstname}/{surename}")     
    @Produces({MediaType.APPLICATION_JSON})     
    public List<Electricity> findByCourseName(@PathParam("firstname") String firstname, @PathParam("surename") String surename) {
        TypedQuery<Electricity> q = em.createQuery("SELECT e FROM Electricity e WHERE e.resid.firstname = :firstname AND e.resid.surename = :surename", Electricity.class);         
        q.setParameter("firstname", firstname);
        q.setParameter("surename", surename);
        return q.getResultList();     
    }
    
    @GET     
    @Path("findByResIdDateHour/{resid}/{usagedate}/{usagehour}")     
    @Produces({MediaType.APPLICATION_JSON})     
    public List<Electricity> findByResIdDateHour(@PathParam("resid") Integer resid, @PathParam("usagedate") String usagedate, @PathParam("usagehour") Integer usagehour) throws Exception{
        List<Electricity> result = new ArrayList<Electricity>();
        Resident resident = new Resident();
        try {
            resident = em.find(resident.getClass(), resid);
            Query query = em.createNamedQuery(Electricity.GET_BY_RESID_DATE_HOUR);
            query.setParameter("resId", resident);
            Date paramDate=new SimpleDateFormat(Constant.DATE_FORMAT).parse(usagedate);
            query.setParameter("usagedate", paramDate);
            query.setParameter("usagehour", usagehour);
            result = query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
        return result;     
    }
    
    @GET
    @Path("findByEmailProvider/{email}/{provider}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Electricity> findByEmailProvider(@PathParam("email") String email, @PathParam("provider") String provider ){          
        Query query = em.createNamedQuery(Electricity.GET_BY_EMAIL_PROVIDER);
        query.setParameter("email", email);
        query.setParameter("provider", provider);
        List<Electricity> result = query.getResultList();
        return result;
    }    
    
    @GET
    @Path("findByResIdDateHourApp/{resid}/{usagedate}/{usagehour}/{app}")
    @Produces ({MediaType.TEXT_PLAIN})
    public String hourUsageOfResident(@PathParam("resid") Integer resid, @PathParam("usagedate") String usagedate, @PathParam("usagehour") Integer usagehour, @PathParam("app") String app) throws Exception{
        String result = "";
        try {
            // Find the record in DB for the hour, for the date and for the resident
            List<Electricity> foundRecords = findByResIdDateHour(resid, usagedate, usagehour);
            
            // Find the data wanted based on appliance name
            if (foundRecords.size() > 0) {
                Electricity el = foundRecords.get(0);
                switch (app){
                    case Constant.FRIDGE:
                        result = Double.toString(el.getFridgeusage().doubleValue());
                        break;
                    case Constant.AIR_CONDITIONER:
                        result = Double.toString(el.getAcusage().doubleValue());
                        break;
                    case Constant.WASHING_MACHINE:
                        result = Double.toString(el.getWmusage().doubleValue());
                        break;
                    default:
                        result = "";
                        break;
                }                        
            } else {
                result = "";
            }
        } catch (Exception ex) {
            throw ex;
        }
        
        return result;
    }
    
    @GET
    @Path("findSumUsage/{resid}/{usagedate}/{usagehour}")
    @Produces ({MediaType.TEXT_PLAIN})
    public String findSumUsage(@PathParam("resid") Integer resid, @PathParam("usagedate") String usagedate, @PathParam("usagehour") Integer usagehour) throws Exception{
        String result = "";
        try {
            // Find the record in DB for the hour, for the date and for the resident
            List<Electricity> foundRecords = findByResIdDateHour(resid, usagedate, usagehour);
            
            // Sum the usage of the three appliance if the record can be found in DB
            if (foundRecords.size() > 0) {
                Electricity el = foundRecords.get(0);
                result = Double.toString(SmartERTools.getTotalUsage(el.getFridgeusage().doubleValue(), 
                                                                    el.getWmusage().doubleValue(), 
                                                                    el.getAcusage().doubleValue()));
            } else {
                result = "";
            }
        } catch (Exception ex) {
            throw ex;
        }
        
        return result;
    }
    
    @GET
    @Path("findByDateHour/{usagedate}/{usagehour}")
    @Produces ({MediaType.APPLICATION_JSON})
    public List<Electricity> findByDateHour(@PathParam("usagedate") String usagedate, @PathParam("usagehour") Integer usagehour) throws Exception {
        List<Electricity> results = new ArrayList<Electricity>();
        try {
            Query query = em.createNamedQuery(Electricity.GET_BY_DATE_HOUR);
            Date paramDate = new SimpleDateFormat(Constant.DATE_FORMAT).parse(usagedate);
            query.setParameter("usagedate", paramDate);
            query.setParameter("usagehour", usagehour);
            results = query.getResultList();
        } catch(Exception ex) {
            throw ex;
        }
        
        return results;
    }
    
    @GET
    @Path("findTotalUsageOfAllResidentByDateAndHour/{usagedate}/{usagehour}")
    @Produces (MediaType.APPLICATION_JSON)
    public List<HourlyUsageInfo> findTotalUsageOfAllResidentByDateAndHour(@PathParam("usagedate") String usagedate, @PathParam("usagehour") Integer usagehour) throws Exception {
        List<HourlyUsageInfo> results = new ArrayList<HourlyUsageInfo>();
        try {
            // Find all usage data by date and hour
            List<Electricity> usageList = findByDateHour(usagedate, usagehour);
            // Inital result list data 
            for (Electricity el : usageList) {
                // Calculate total usage for this resident at this hour on this particular day
                double totalUsage = SmartERTools.getTotalUsage(el.getFridgeusage().doubleValue(), 
                                                               el.getAcusage().doubleValue(),
                                                               el.getWmusage().doubleValue());
                int resid = el.getResid().getResid();
                String address = el.getResid().getAddress();
                Short postcode = el.getResid().getPostcode();
                // Add this record into result list
                results.add(new HourlyUsageInfo(resid, address, postcode, totalUsage));
            }
        } catch (Exception ex) {
            throw ex;
        }
        return results;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
