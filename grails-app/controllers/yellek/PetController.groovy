package yellek

import grails.gorm.transactions.Transactional
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class PetController {

    PetService petService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Transactional
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond petService.list(params), model:[petCount: petService.count()]
    }

    def show(Long id) {
        respond petService.get(id)
    }

    def create() {
        respond new Pet(params)
    }

    def save(Pet pet) {
        if (pet == null) {
            notFound()
            return
        }

        try {
            petService.save(pet)
        } catch (ValidationException e) {
            respond pet.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'pet.label', default: 'Pet'), pet.id])
                redirect pet
            }
            '*' { respond pet, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond petService.get(id)
    }

    def update(Pet pet) {
        if (pet == null) {
            notFound()
            return
        }

        try {
            petService.save(pet)
        } catch (ValidationException e) {
            respond pet.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'pet.label', default: 'Pet'), pet.id])
                redirect pet
            }
            '*'{ respond pet, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        petService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'pet.label', default: 'Pet'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'pet.label', default: 'Pet'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
