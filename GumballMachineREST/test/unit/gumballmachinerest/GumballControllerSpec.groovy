package gumballmachinerest



import grails.test.mixin.*
import spock.lang.*

@TestFor(Gumball1Controller)
@Mock(Gumball1)
class GumballControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.gumballInstanceList
            model.gumballInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.gumballInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def gumball = new Gumball1()
            gumball.validate()
            controller.save(gumball)

        then:"The create view is rendered again with the correct model"
            model.gumballInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            gumball = new Gumball1(params)

            controller.save(gumball)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/gumball/show/1'
            controller.flash.message != null
            Gumball1.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def gumball = new Gumball1(params)
            controller.show(gumball)

        then:"A model is populated containing the domain instance"
            model.gumballInstance == gumball
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def gumball = new Gumball1(params)
            controller.edit(gumball)

        then:"A model is populated containing the domain instance"
            model.gumballInstance == gumball
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/gumball/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def gumball = new Gumball1()
            gumball.validate()
            controller.update(gumball)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.gumballInstance == gumball

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            gumball = new Gumball1(params).save(flush: true)
            controller.update(gumball)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/gumball/show/$gumball.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/gumball/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def gumball = new Gumball1(params).save(flush: true)

        then:"It exists"
            Gumball1.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(gumball)

        then:"The instance is deleted"
            Gumball1.count() == 0
            response.redirectedUrl == '/gumball/index'
            flash.message != null
    }
}
