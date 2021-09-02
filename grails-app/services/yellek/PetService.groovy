package yellek

import grails.gorm.services.Service
import groovy.transform.CompileStatic

@CompileStatic
@Service(Pet)
interface PetService {

    Pet get(Serializable id)

    List<Pet> list(Map args)

    Long count()

    void delete(Serializable id)

    Pet save(Pet pet)

}