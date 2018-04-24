package smartEREntities.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
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
import javax.ws.rs.core.*;
import smartER.DAL.Constant;
import smartER.DAL.SmartERTools;
import smartEREntities.Electricity;
import smartEREntities.HourlyUsageInfo;
import smartEREntities.PeakUsageHourInfo;
import smartEREntities.Resident;
import smartEREntities.TotalUsageForEachAppIn24H;

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
    
    @POST
    @Path("createMulitple")
    @Consumes({MediaType.APPLICATION_JSON})
    public void createMultiple(List<Electricity> entityList) {
        for (Electricity entity : entityList) {
            super.create(entity);
        }
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
    public List<Electricity> findByResidentFullName(@PathParam("firstname") String firstname, @PathParam("surename") String surename) {
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
                result = Double.toString(el.getTotalUsage());
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
                double totalUsage = el.getTotalUsage();
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
    
    @GET
    @Path("findPeakHourByResid/{resid}")
    @Produces (MediaType.APPLICATION_JSON)
    public List<PeakUsageHourInfo> findPeakHourByResid(@PathParam("resid") Integer resid) {
        List<PeakUsageHourInfo> results = new ArrayList<>(0);
        
        // Find all usage records by resid
        List<Electricity> allUsages = findByResId(resid);
        // Sort usage list
        Collections.sort(allUsages);
        // Get highest usage value
        double peakUsage = allUsages.get(allUsages.size() - 1).getTotalUsage();
        
        // loop sorted list of usages and find out all peak hours and put them in result list
        for (int index = allUsages.size() - 1; index >= 0 ; index--){
            Electricity el = allUsages.get(index);
            if(peakUsage == el.getTotalUsage()) {
                PeakUsageHourInfo peakInfo = new PeakUsageHourInfo(el.getUsagedate(), el.getUsagehour(), el.getTotalUsage());
                results.add(peakInfo);
            } else if (peakUsage > el.getTotalUsage()) {
                break;
            }
        }
        return results;
    }
    
    @GET
    @Path("findTotalUsageForEachAppIn24H/{resid}/{usagedate}")
    @Produces (MediaType.APPLICATION_JSON)
    public TotalUsageForEachAppIn24H findTotalUsageForEachAppIn24H(@PathParam("resid") Integer resid, @PathParam("usagedate") String usagedate) throws Exception {
        TotalUsageForEachAppIn24H result = null;
        try {
            // Get usage list by resid and date
            List<Electricity> usageList = SmartERTools.getUsageByResidAndDate(resid, usagedate, em);
            
            // Calculate total usage for all appliances 
            double fridgeUsage = SmartERTools.getFridgeTotalUsage(usageList);
            double wmUsage = SmartERTools.getWMTotalUsage(usageList);
            double acUsage = SmartERTools.getACTotalUsage(usageList);
            
            // Initialize reture object
            result = new TotalUsageForEachAppIn24H(resid, fridgeUsage, acUsage, wmUsage);
        } catch (Exception ex) {
            throw ex;
        }
        
        return result;
    }
    
    @GET
    @Path("findDailyOrHourlyUsage/{resid}/{usagedate}/{viewType}")
    @Produces (MediaType.APPLICATION_JSON)
    public Object findDailyOrHourlyUsage(@PathParam("resid") Integer resid, @PathParam("usagedate") String usagedate, @PathParam("viewType") String viewType) throws Exception {
        Object result = null;
        try {
            // Get usage list by resid and date
            List<Electricity> usageList = SmartERTools.getUsageByResidAndDate(resid, usagedate, em);
            
            if (Constant.VIEW_HOURLY.equals(viewType)) {
                // Create JSON builder
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                // Loop usage list and construct json objects
                for (Electricity el :usageList) {
                    // Create JSON object
                    JsonObjectBuilder jObjectBuilder = Json.createObjectBuilder();
                    jObjectBuilder.add(Constant.JSON_KEY_RESID, Integer.toString(el.getResid().getResid()));
                    jObjectBuilder.add(Constant.JSON_KEY_USAGE, Double.toString(el.getTotalUsage()));
                    jObjectBuilder.add(Constant.JSON_KEY_TEMPERATURE, Integer.toString(el.getTemperature()));
                    jObjectBuilder.add(Constant.JSON_KEY_DATE, usagedate);
                    jObjectBuilder.add(Constant.JSON_KEY_TIME, Integer.toString(el.getUsagehour()));
                    arrayBuilder.add(jObjectBuilder.build());
                }
                // Return result list
                result = arrayBuilder.build();
            } else if (Constant.VIEW_DAILY.equals(viewType)) {
                // variable sum usage of 24H
                double sumUsage = 0.0;
                double sumTemperature = 0.0;
                for (Electricity el :usageList) {
                    // Add this hour usage to sum usage 
                    sumUsage += el.getTotalUsage();
                    // Add this hour temperature to sum temperature
                    sumTemperature += el.getTemperature();
                }
                
                // Create JSON object
                JsonObjectBuilder jObjectBuilder = Json.createObjectBuilder();
                jObjectBuilder.add(Constant.JSON_KEY_RESID, Integer.toString(resid));
                jObjectBuilder.add(Constant.JSON_KEY_USAGE, Double.toString(SmartERTools.round(sumUsage, 2)));
                jObjectBuilder.add(Constant.JSON_KEY_TEMPERATURE, Double.toString(SmartERTools.round(sumTemperature / 24, 2)));
                result = jObjectBuilder.build();
            } 
        } catch (Exception ex) {
            throw ex;
        } 
        return result;
    }
    
    @GET
    @Path("findDailyOrHourlyUsageForAll/{usagedate}/{viewType}")
    @Produces (MediaType.APPLICATION_JSON)
    public Object findDailyOrHourlyUsageForAllResidents(@PathParam("usagedate") String usagedate, @PathParam("viewType") String viewType) throws Exception {
        Object result = null;
        try {
            // Get usage list by resid and date
            List<Electricity> usageList = SmartERTools.getUsageByDateForAllResident(usagedate, em);
            
            if (Constant.VIEW_HOURLY.equals(viewType)) {
                // Create JSON builder
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                // Loop usage list and construct json objects
                for (Electricity el :usageList) {
                    // Create JSON object
                    JsonObjectBuilder jObjectBuilder = Json.createObjectBuilder();
                    jObjectBuilder.add(Constant.JSON_KEY_RESID, Integer.toString(el.getResid().getResid()));
                    jObjectBuilder.add(Constant.JSON_KEY_USAGE, Double.toString(el.getTotalUsage()));
                    jObjectBuilder.add(Constant.JSON_KEY_TIME, Integer.toString(el.getUsagehour()));
                    arrayBuilder.add(jObjectBuilder.build());
                }
                // Return result list
                result = arrayBuilder.build();
            } else if (Constant.VIEW_DAILY.equals(viewType)) {
                // Create JSON builder
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                // variable sum usage of 24H and resident id
                double sumUsage = 0.0;
                int resid = usageList.get(0).getResid().getResid();
                for (Electricity el :usageList) {
                    // set resid
                    if (resid != el.getResid().getResid()) {
                        // construct daily usage JSON object for the current resident
                        JsonObjectBuilder jObjectBuilder = Json.createObjectBuilder();
                        jObjectBuilder.add(Constant.JSON_KEY_RESID, Integer.toString(resid));
                        jObjectBuilder.add(Constant.JSON_KEY_USAGE, Double.toString(SmartERTools.round(sumUsage, 2)));
                        arrayBuilder.add(jObjectBuilder.build());
                        // reset sum usage of 24H and resident Id for new resident
                        resid = el.getResid().getResid();
                        System.out.println("*****resid:" + resid);
                        sumUsage = 0.0;
                    }
                    // Add this hour usage to sum usage 
                    sumUsage += el.getTotalUsage();
                }
                
                // add the last resident data in JSON array
                JsonObjectBuilder jObjectBuilder = Json.createObjectBuilder();
                jObjectBuilder.add(Constant.JSON_KEY_RESID, Integer.toString(resid));
                jObjectBuilder.add(Constant.JSON_KEY_USAGE, Double.toString(SmartERTools.round(sumUsage, 2)));
                arrayBuilder.add(jObjectBuilder.build());
                
                // Create JSON object
                result = arrayBuilder.build();
            } 
        } catch (Exception ex) {
            throw ex;
        } 
        return result;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
