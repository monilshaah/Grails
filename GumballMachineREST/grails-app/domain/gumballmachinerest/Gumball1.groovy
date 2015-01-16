package gumballmachinerest

import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode
import grails.rest.*

//@ToString(includeNames = true, includeFields = true)
//@EqualsAndHashCode
@Resource(uri='/gumball',formats=['json'])
class Gumball1 {

    String modelNumber
    String serialNumber
    Integer countGumballs

    static constraints = {
        serialNumber(unique: true)
    }
}
