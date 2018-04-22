package smartEREntities.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
import smartEREntities.Credential;
import smartER.DAL.MD5Tools;
import smartEREntities.Resident;

/**
 *
 * @author William
 */
@Stateless
@Path("smarterentities.credential")
public class CredentialFacadeREST extends AbstractFacade<Credential> {

    @PersistenceContext(unitName = "SmartER-warPU")
    private EntityManager em;

    public CredentialFacadeREST() {
        super(Credential.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Credential entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Credential entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Credential find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credential> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credential> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("findByPassword/{pwd}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credential> findByPassword(@PathParam("pwd") String pwd){          
        Query query = em.createNamedQuery(Credential.GET_BY_PASSWORD);
        query.setParameter("passwordhash", MD5Tools.encrypt(pwd));
        List<Credential> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("findByUserNamePassword/{usearname}/{pwd}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Credential> findByUserNamePassword(@PathParam("usearname") String username, @PathParam("pwd") String pwd){          
        Query query = em.createNamedQuery(Credential.GET_BY_USERNAME_PASSWORD);
        query.setParameter("username", username);
        query.setParameter("passwordhash", pwd);
        List<Credential> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("findByRegistrateDT/{rgDate}") 
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credential> findByRegistrateDT(@PathParam("rgDate") String rgDate) throws Exception{ 
        List<Credential> result = new ArrayList<Credential>();
        try{
            Query query = em.createNamedQuery(Credential.GET_BY_REGISTRATION_DATE);
            Date paramDate=new SimpleDateFormat(Constant.DATE_FORMAT).parse(rgDate);
            query.setParameter("registrationdate", paramDate);
            result.addAll(query.getResultList());
        } catch (Exception ex) {
            throw ex;
        }
        return result;
    }
    
    @GET
    @Path("findByResId/{resid}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credential> findByResId(@PathParam("resid") Integer resid){ 
        Resident resident = new Resident();
        resident = em.find(resident.getClass(), resid);
        Query query = em.createNamedQuery(Credential.GET_BY_RESID);
        query.setParameter("resId", resident);
        List<Credential> result = query.getResultList();
        return result;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
