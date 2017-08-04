package demo

import grails.http.client.RxHttpClientBuilder
import grails.test.mixin.integration.Integration
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import static grails.http.HttpHeader.CONTENT_TYPE
import static grails.http.HttpStatus.CREATED
import static grails.http.HttpStatus.OK
import static grails.http.MediaType.JSON

@Integration
@Stepwise
class MusicFunctionalSpec extends Specification {

    @Shared RxHttpClientBuilder rest = new RxHttpClientBuilder()

    void "test that no albums exist"() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/albums").toBlocking().first()

        then:
        resp.status == OK
        resp.header(CONTENT_TYPE.toString()).contains JSON.toString()
        resp.toJson().toBlocking().first().size() == 0
    }

    void "test creating albums"() {
        when:
        def resp = rest.post("http://localhost:${serverPort}/albums") {
            json {
                artistName 'King Crimson'
                title 'Red'
                genre 'PROGRESSIVE_ROCK'
            }
        }.toBlocking().first()

        def jsonResult = resp.toJson().toBlocking().first()

        then:
        resp.status == CREATED
        resp.header(CONTENT_TYPE.toString()).contains JSON.toString()

        and:
        jsonResult.artistName == 'King Crimson'
        jsonResult.title == 'Red'
        jsonResult.genre == 'PROGRESSIVE_ROCK'

        when:
        resp = rest.post("http://localhost:${serverPort}/albums") {
            json {
                artistName 'Riverside'
                title 'Love, Fear and the Time Machine'
                genre 'PROGRESSIVE_ROCK'
            }
        }.toBlocking().first()

        jsonResult = resp.toJson().toBlocking().first()

        then:
        resp.status == CREATED
        resp.header(CONTENT_TYPE.toString()).contains JSON.toString()

        and:
        jsonResult.artistName == 'Riverside'
        jsonResult.title == 'Love, Fear and the Time Machine'
        jsonResult.genre == 'PROGRESSIVE_ROCK'

        when:
        resp = rest.post("http://localhost:${serverPort}/albums") {
            json {
                artistName 'Johnny Winter'
                title 'Progressive Blues Experiment'
                genre 'BLUES'
            }
        }.toBlocking().first()

        jsonResult = resp.toJson().toBlocking().first()

        then:
        resp.status == CREATED
        resp.header(CONTENT_TYPE.toString()).contains JSON.toString()

        and:
        jsonResult.artistName == 'Johnny Winter'
        jsonResult.title == 'Progressive Blues Experiment'
        jsonResult.genre == 'BLUES'


        when:
        resp = rest.post("http://localhost:${serverPort}/albums") {
            json {
                artistName 'Motorhead'
                title "No Sleep 'til Hammersmith"
                genre 'HEAVY_METAL'
            }
        }.toBlocking().first()

        jsonResult = resp.toJson().toBlocking().first()

        then:
        resp.status == CREATED
        resp.header(CONTENT_TYPE.toString()).contains JSON.toString()

        and:
        jsonResult.artistName == 'Motorhead'
        jsonResult.title == "No Sleep 'til Hammersmith"
        jsonResult.genre == 'HEAVY_METAL'
    }


    void 'test retrieving list of albums defaults to JSON'() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/albums").toBlocking().first()

        def jsonResult = resp.toJson().toBlocking().first()

        then:
        resp.status == OK
        resp.header(CONTENT_TYPE.toString()).contains JSON.toString()
        jsonResult.size() == 4

        and:
        jsonResult[0].artistName == 'King Crimson'
        jsonResult[0].title == 'Red'
        jsonResult[0].genre == 'PROGRESSIVE_ROCK'

        and:
        jsonResult[1].artistName == 'Riverside'
        jsonResult[1].title == 'Love, Fear and the Time Machine'
        jsonResult[1].genre == 'PROGRESSIVE_ROCK'

        and:
        jsonResult[3].artistName == 'Motorhead'
        jsonResult[3].title == "No Sleep 'til Hammersmith"
        jsonResult[3].genre == 'HEAVY_METAL'

        and:
        jsonResult[2].artistName == 'Johnny Winter'
        jsonResult[2].title == 'Progressive Blues Experiment'
        jsonResult[2].genre == 'BLUES'
    }
    // tag::genre_query[]
    void "test retrieving albums by genre"() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/genre/PROGRESSIVE_ROCK/albums")  // <1>
                .toBlocking().first()

        def jsonResult = resp.toJson().toBlocking().first()

        then:
        resp.status == OK
        resp.header(CONTENT_TYPE.toString()).contains JSON.toString()
        jsonResult.size() == 2                                                               // <2>

        when:
        resp = rest.get("http://localhost:${serverPort}/genre/HEAVY_METAL/albums")           // <3>
                .toBlocking().first()

        jsonResult = resp.toJson().toBlocking().first()

        then:
        resp.status == OK
        resp.header(CONTENT_TYPE.toString()).contains JSON.toString()
        jsonResult.size() == 1                                                               // <4>

        when:
        resp = rest.get("http://localhost:${serverPort}/genre/BLUES/albums")                 // <5>
                .toBlocking().first()

        jsonResult = resp.toJson().toBlocking().first()

        then:
        resp.status == OK
        resp.header(CONTENT_TYPE.toString()).contains JSON.toString()
        jsonResult.size() == 1                                                               // <6>
    }
    // end::genre_query[]
}
