class UrlMappings {

	static mappings = {
        
		
		"/$controller/$action?/$id?(.$format)?"{
			action = [GET:"gumballMachine", POST:"insertCoin", PUT:"turnCrank"]
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
