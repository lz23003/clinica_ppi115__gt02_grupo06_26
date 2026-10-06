package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import java.util.List;

public interface DAOInterface<T> {

    /**
     * Persiste un nuevo registro en el repositorio
     *
     * @param registro entidad a persistir
     * @throws IllegalArgumentException si el registro es nulo
     * @throws IllegalStateException si ocurre un problema al persistir la
     * entidad
     */
    public void crear(T registro) throws IllegalArgumentException, IllegalStateException;

    /**
     * Modificar un registro en el repositorio, y lo devuelve con las
     * actualizaciones aplicadas
     *
     * @param registro el registro a modificar
     * @return El registro modificado
     * @throws IllegalArgumentException Si el registro es nulo
     * @throws IllegalStateException Si existe un problema para modificar la
     * entidad. No puede accederse al repositorio
     */
    public T modificar(T registro) throws IllegalArgumentException, IllegalStateException;

    // public void actualizar( T registro) throws IllegalArgumentException, IllegalStateException;
    /**
     * Elimina un registro del repositorio
     *
     * @param registro entidad a eliminar
     * @throws IllegalArgumentException si el resgistro es nulo
     * @throws IllegalStateException si existe un problema para eliminar la
     * entidad
     */
    public void eliminar(T registro) throws IllegalArgumentException, IllegalStateException;

    /*
     * Busca un registro por su identificador
     *
     * @param id identificador de la entidad
     * @return la entidad encontrada o null si no existe
     * @throws IllegalArgumentException si el id es null
     */
    public T buscarPorId(Object id) throws IllegalArgumentException, IllegalStateException;

    /*
        * Obtiene un rango de registros
     *
     * @param first indice inicial de los registros
     * @param max número máximo de resultados a devolver
     * @return lista de entidades en el rango indicado
     * @throws IllegalArgumentException si los parametros son invalidos (first < 0, max <= 0)
     */
    public List<T> findRange(int first, int max) throws IllegalArgumentException, IllegalStateException;

    /*
         * Cuenta la cantidad total de registros en el repositorio
     *
     * @return el número total de registros
     * @throws IllegalArgumentException si ocurre un problema al contar las entidades
     */
    public int count() throws IllegalStateException;

    public List<T> findAll() throws IllegalStateException;

}
