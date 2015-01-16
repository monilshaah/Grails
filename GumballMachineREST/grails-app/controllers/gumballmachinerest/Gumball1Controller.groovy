package gumballmachinerest

import static org.springframework.http.HttpStatus.*
import grails.converters.JSON
import grails.transaction.Transactional
import gumball.GumballMachine;

@Transactional
class Gumball1Controller {
	def String machineSerialNum = "1234998871109"
	def String machineModelNum = "12345"
	def GumballMachine gumballMachine

	def gumballMachine() {
		println "***in gumballMachine()..."
		def gumball = Gumball.findBySerialNumber( machineSerialNum )
		if ( gumball )
		{
			// create a default machine
			gumballMachine = new GumballMachine(gumball.modelNumber, gumball.serialNumber)
			System.out.println(gumballMachine.getAbout())
		}
		else
		{
			gumball = new Gumball()
			gumball.serialNumber = machineSerialNum
			gumball.modelNumber = machineModelNum
			gumball.countGumballs = 5
			gumball.save(flush: true)
			println '***Gumaball created...!!!'
			gumballMachine = new GumballMachine(gumball.modelNumber, gumball.serialNumber)
			println '***GumballMachine retrieved...'+gumballMachine
			//flash.message = "Error! Gumball Machine Not Found!"
			//render(view: "index")
		}
		def returnMap = [:]
		returnMap.putAt('state', gumballMachine.getCurrentState())
		returnMap.putAt('model', gumball.modelNumber)
		returnMap.putAt('serial', gumball.serialNumber)
		returnMap.putAt('gumballCount', gumball.countGumballs)
		returnMap.putAt('hash', (gumballMachine.getCurrentState()+gumball.modelNumber+gumball.serialNumber+gumball.countGumballs).encodeAsSHA256())
		
		render returnMap as JSON
	}
	
	def insertCoin() {
		def requestStatus = null
		// dump out request object
		request.each { key, value ->
			println( "request: $key = $value")
		}

		// dump out params
		params?.each { key, value ->
			println( "params: $key = $value" )
		}
		
		// don't get machine from session
		// gumballMachine = session.machine

		// restore machine to client state (instead)
		def state = params?.state
		def modelNum = params?.model
		def serialNum = params?.serial
		def gumballCount = params?.gumballCount
		def hash = params?.hash
		
		def reqHash = (state+modelNum+serialNum+gumballCount).encodeAsSHA256()
		println "string: "+state+modelNum+serialNum+gumballCount
		println "reqHash: "+reqHash
		println "hash   : "+hash
		
		if (hash.equals(reqHash)) {
			requestStatus = 'valid'
			gumballMachine = new GumballMachine(modelNum, serialNum) ;
			gumballMachine.setCurrentState(state) ;
		
			System.out.println(gumballMachine.getAbout())
		
			gumballMachine.insertCoin()
		}
		else {
			requestStatus = 'invalid'
		}
		
		def returnMap = [:]
		returnMap.putAt('state', gumballMachine.getCurrentState())
		returnMap.putAt('model', modelNum)
		returnMap.putAt('serial', serialNum)
		returnMap.putAt('gumballCount', gumballCount)
		returnMap.putAt('status', requestStatus)
		returnMap.putAt('hash', (gumballMachine.getCurrentState()+modelNum+serialNum+gumballCount).encodeAsSHA256())
		
		render returnMap as JSON
	}
	
	def turnCrank() {
		def requestStatus = null
		// dump out request object
		request.each { key, value ->
			println( "request: $key = $value")
		}

		// dump out params
		params?.each { key, value ->
			println( "params: $key = $value" )
		}
		
		// don't get machine from session
		// gumballMachine = session.machine

		// restore machine to client state (instead)
		def state = params?.state
		def modelNum = params?.model
		def serialNum = params?.serial
		def gumballCount = params?.gumballCount
		def hash = params?.hash
		
		def reqHash = (state+modelNum+serialNum+gumballCount).encodeAsSHA256()
		println "string: "+state+modelNum+serialNum+gumballCount
		println "reqHash: "+reqHash
		println "hash   : "+hash
		
		if (hash.equals(reqHash)) {
			requestStatus = 'valid'
			gumballMachine = new GumballMachine(modelNum, serialNum) ;
			gumballMachine.setCurrentState(state) ;
		
			System.out.println(gumballMachine.getAbout())
		
			gumballMachine.crankHandle();
				
			if ( gumballMachine.getCurrentState().equals("gumball.CoinAcceptedState") ) {
				def gumball = Gumball.findBySerialNumber( machineSerialNum )
				if ( gumball) {					
					// gumball.lock() // pessimistic lock
					if ( gumball.countGumballs > 0)
						gumball.countGumballs-- ;
					gumball.save(flush: true); // default optimistic lock
					gumballCount = gumball.countGumballs
				}
			}
		}
		else {
			requestStatus = 'invalid'
		}
		
		def returnMap = [:]
		returnMap.putAt('state', gumballMachine.getCurrentState())
		returnMap.putAt('model', modelNum)
		returnMap.putAt('serial', serialNum)
		returnMap.putAt('gumballCount', gumballCount)
		returnMap.putAt('status', requestStatus)
		returnMap.putAt('hash', (gumballMachine.getCurrentState()+modelNum+serialNum+gumballCount).encodeAsSHA256())
		
		render returnMap as JSON
	}
	
 /*   @Transactional
    def save(Gumball gumballInstance) {
        if (gumballInstance == null) {
            notFound()
            return
        }

        if (gumballInstance.hasErrors()) {
            respond gumballInstance.errors, view:'create'
            return
        }

        gumballInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'gumball.label', default: 'Gumball'), gumballInstance.id])
                redirect gumballInstance
            }
            '*' { respond gumballInstance, [status: CREATED] }
        }
    }

    def edit(Gumball gumballInstance) {
        respond gumballInstance
    }

    @Transactional
    def update(Gumball gumballInstance) {
        if (gumballInstance == null) {
            notFound()
            return
        }

        if (gumballInstance.hasErrors()) {
            respond gumballInstance.errors, view:'edit'
            return
        }

        gumballInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Gumball.label', default: 'Gumball'), gumballInstance.id])
                redirect gumballInstance
            }
            '*'{ respond gumballInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Gumball gumballInstance) {

        if (gumballInstance == null) {
            notFound()
            return
        }

        gumballInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Gumball.label', default: 'Gumball'), gumballInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'gumball.label', default: 'Gumball'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }*/
}
