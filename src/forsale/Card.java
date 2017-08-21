package forsale;

/**
 * An enum representing property cards for the game "For Sale"
 *
 * @author Michael Albert
 */
public enum Card  {

    BOX(1, "Box"),
    LONGDROP(2, "Longdrop"),
    MANHOLE(3, "Manhole"),
    DOGHOUSE(4, "Doghouse"),
    CAVE(5, "Cave"),
    TEEPEE(6, "Teepee"),
    TENT(7, "Tent"),
    IGLOO(8, "Igloo"),
    BOATHOUSE(9, "Boathouse"),
    SHACK(10, "Shack"),
    TREEHOUSE(11, "Treehouse"),
    PUEBLO(12, "Pueblo"),
    CRIB(13, "Crib"),
    CAMPERVAN(14, "Camper van"),
    LOGCABIN(15, "Log cabin"),
    LIGHTHOUSE(16, "Lighthouse"),
    RIVERBOAT(17, "Riverboat"),
    MOTORHOME(18, "Motor home"),
    FLAT(19, "Flat"),
    CONDOMINIUM(20, "Condominium"),
    BUNGALOW(21, "Bungalow"),
    ESTATE(22, "Estate"),
    HERITAHE(23,"Heritage"),
    BEACHFRONT(24, "Beachfront"),
    MANOR(25, "Manor"),
    FORT(26, "Fort"),
    PALACE(27, "Palace"),
    CASTLE(28, "Castle"),
    SKYSCRAPER(29, "Skyscraper"),
    SPACESTATION(30, "Space station");
    
    private final int quality;
    private final String name;

    Card(int quality, String name) {
        this.quality = quality;
        this.name = name;

    }

    public int getQuality() {
        return quality;
    }

    @Override
    public String toString() {
        return name;
    }
   
}
