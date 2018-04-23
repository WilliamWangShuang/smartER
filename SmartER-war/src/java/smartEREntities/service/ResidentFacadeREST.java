package smartEREntities.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.criteria.*;
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
import smartEREntities.Resident;

/**
 *
 * @author William
 */
@Stateless
@Path("smarterentities.resident")
public class ResidentFacadeREST extends AbstractFacade<Resident> {

    @PersistenceContext(unitName = "SmartER-warPU")
    private EntityManager em;

    public ResidentFacadeREST() {
        super(Resident.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Resident entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Resident entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public Resident find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Resident> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Resident> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("findByFirstName/{first_name}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Resident> findByFirstName(@PathParam("first_name") String firstName){          
        Query query = em.createNamedQuery(Resident.GET_BY_FIRST_NAME);
        query.setParameter("firstname", firstName);
        List<Resident> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("findBySureName/{sure_name}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Resident> findBySureName(@PathParam("sure_name") String sureName){ 
        Query query = em.createNamedQuery(Resident.GET_BY_SURE_NAME);
        query.setParameter("surename", sureName);
        List<Resident> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("findByDOB/{birthday}") 
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Resident> findByDOB(@PathParam("birthday") String dob) throws Exception{ 
        List<Resident> result = new ArrayList<Resident>();
        try{
            Query query = em.createNamedQuery(Resident.GET_BY_DOB);
            Date paramDate=new SimpleDateFormat(Constant.DATE_FORMAT).parse(dob);
            query.setParameter("dob", paramDate);
            result.addAll(query.getResultList());
        } catch (Exception ex) {
            throw ex;
        }
        return result;
    }
    
    @GET
    @Path("findByAddress/{address}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Resident findByAddress(@PathParam("address") String address){ 
        Query query = em.createNamedQuery(Resident.GET_BY_ADDRESS);
        query.setParameter("address", address);
        Resident result = query.getResultList().size() > 0 ? (Resident)query.getResultList().get(0) : null;
        return result;
    }
    
    @GET
    @Path("findByPostcode/{postcode}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Resident> findByPostcode(@PathParam("postcode") Integer postcode){          
        Query query = em.createNamedQuery(Resident.GET_BY_POSTCODE);
        query.setParameter("postcode", postcode);
        List<Resident> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("findByEmail/{email}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Resident> findByEmail(@PathParam("email") String email){          
        Query query = em.createNamedQuery(Resident.GET_BY_EMAIL);
        query.setParameter("email", email);
        List<Resident> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("findByMobile/{mobile}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Resident> findByMobile(@PathParam("mobile") String mobile){          
        Query query = em.createNamedQuery(Resident.GET_BY_MOBILE);
        query.setParameter("mobile", mobile);
        List<Resident> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("findByNoOfResident/{number}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Resident> findByNoOfResident(@PathParam("number") Integer number){          
        Query query = em.createNamedQuery(Resident.GET_BY_NUMBER_OF_RESIDENT);
        query.setParameter("numberofresident", number);
        List<Resident> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("findByEnergyProvider/{provider}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Resident> findByEnergyProvider(@PathParam("provider") String provider){          
        Query query = em.createNamedQuery(Resident.GET_BY_PROVIDER);
        query.setParameter("energyprovider", provider);
        List<Resident> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("findByFullName/{firstname}/{surename}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Resident> findByFullName(@PathParam("firstname") String firstname, @PathParam("surename") String surename){
        List<Resident> result = new ArrayList<>();
        Predicate conditionFName = null;
        Predicate conditionSName = null;
        Predicate condition = null;
        
        CriteriaBuilder qb = em.getCriteriaBuilder();    // Create query builder
        CriteriaQuery<Resident> c = qb.createQuery(Resident.class);   // Create query
        Root<Resident> t = c.from(Resident.class);    // Add <Query> node in query
        conditionFName = qb.equal(t.<String>get("firstname"), firstname); 
        conditionSName = qb.equal(t.<String>get("surename"), surename);
        condition = qb.and(conditionFName, conditionSName);
        
        c.where(condition);     // Create WHERE clause
        TypedQuery<Resident> q = em.createQuery(c);   // Add WHERE clause into query
        result = q.getResultList();   // Execute query
        
        return result;
    }
        
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
