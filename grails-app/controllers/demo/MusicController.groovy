package demo

import grails.rest.RestfulController

class MusicController extends RestfulController<Album> {

    static def responseFormats = ['json', 'xml']

    public MusicController() {
        super(Album)
    }

    @Override
    protected List<Album> listAllResources(Map m) {
        // don't use params
        if (m.genre) {
            Album.where {
                genre == m.genre
            }.findAll()
        } else {
            resource.list(m)
        }
        /*
        Album.where {
          if(m.genre) {
            genre == m.genre
           }
         }.list()
         */
    }
}
