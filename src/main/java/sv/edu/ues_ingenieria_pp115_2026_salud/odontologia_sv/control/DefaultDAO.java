
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;


//T tipo de entidad

public abstract class DefaultDAO<T> implements DAOInterface<T>{


    /*
    metodo que debe ser implementado por las subclases
    para proporcionar en EntityManager correspondiente
    */
    protected abstract EntityManager getEntityManager();
    
  
    
    final Class<T> entityClass;

    public DefaultDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
   
    
    
    @Override
    public void crear(T registro)throws IllegalArgumentException, IllegalStateException {
        if(registro==null){
            throw new IllegalArgumentException("El registro no puede ser nulo");
        }
        
        try{
           getEntityManager().persist(registro);
           getEntityManager().flush();
            
        }catch(Exception ex){
            throw new IllegalStateException("Error al crear el registro "+ex);
        }

        

    }

    @Override
    public T modificar(T registro) throws IllegalArgumentException, IllegalStateException {
       if(registro==null){
           throw new IllegalArgumentException("El registro no puede ser nulo");
       }
       
        try {
           
           
            
            T entidad=getEntityManager().merge(registro);
            getEntityManager().flush();
            return entidad;
            
        } catch (Exception ex) {
            throw new IllegalStateException("Error al modificar el registro "+ex);
        }
        

    }

    @Override
    public void eliminar(T registro) throws IllegalArgumentException, IllegalStateException {
        if(registro==null){
            throw new IllegalArgumentException("El registro no puede ser nulo");
        }
        
        try{
          //verificar que el objeto a eliminar este gestionado
                T entidad=getEntityManager().merge(registro);
                getEntityManager().remove(entidad);
                
           
        
        }catch(Exception ex){
            throw new IllegalStateException("Error al eliminar el registro "+ex);
        }
        
    }

    @Override
    public T buscarPorId(Object id) throws IllegalArgumentException, IllegalStateException {
        if(id==null){
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        
        try{
           
            
            return getEntityManager().find(entityClass, id);
        
        }catch(Exception ex){
            throw new IllegalStateException("Error al buscar por id "+ex);
        }

    }

    @Override
    public List<T> findRange(int first, int max) throws IllegalArgumentException, IllegalStateException {
        
        if(first<0 || max<=0){
            throw new IllegalArgumentException("Los parametros first y max deben de ser validos (first>=0, max>0)");
        }
        
        EntityManager em=getEntityManager();
        
        try {
           
            
            CriteriaBuilder cb=em.getCriteriaBuilder();
            CriteriaQuery<T> cq=cb.createQuery(entityClass);
            Root<T> root=cq.from(entityClass);
            
            TypedQuery<T> query=em.createQuery(cq);
            query.setFirstResult(first); //desde que fila iniciar
            query.setMaxResults(max);   //cuantos resultados traer
            
            return query.getResultList();
            
            
        } catch (Exception e) {
            throw new IllegalStateException("Error al ejecutar findRange");
        }
        
    }
    

    @Override
    public int count() throws IllegalStateException {
        
         EntityManager em=getEntityManager();
       
       
       try{
          CriteriaBuilder cb=em.getCriteriaBuilder();
         CriteriaQuery<Long> cq=cb.createQuery(Long.class);
         Root<T> root=cq.from(entityClass);
         
        cq.select(cb.count(root));
        TypedQuery<Long> query=em.createQuery(cq);
        Long total=query.getSingleResult();
        return total.intValue();
          
       }catch(Exception ex){
          throw new IllegalStateException("Error al contar los registros ");
       }

    }
    
   
    

    @Override
    public List<T> findAll() throws IllegalStateException {
        
        EntityManager em=getEntityManager();

        
        try{
            CriteriaBuilder cb=em.getCriteriaBuilder();
            CriteriaQuery<T> cq=cb.createQuery(entityClass);
            Root<T> root=cq.from(entityClass);
            cq.select(root);
            
            return em.createQuery(cq).getResultList();
        }catch(Exception ex){
           throw new IllegalStateException("Error al obtener todos los registros", ex);
        }
        

    }

    
}
