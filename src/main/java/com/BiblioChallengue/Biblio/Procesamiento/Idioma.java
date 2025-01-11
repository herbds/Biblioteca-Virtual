package com.BiblioChallengue.Biblio.Procesamiento;

public enum Idioma {
    ES("es", "Español"),
    EN("en", "Inglés"),
    TL("tl", "Tagalo/Tailandés"),
    FR("fr", "Francés"),
    DE("de", "Alemán"),
    PT("pt", "Portugués"),
    IT("it", "Italiano"),
    NL("nl", "Neerlandés"),
    LA("la", "Latín"),
    RU("ru", "Ruso"),
    ZH("zh", "Chino"),
    JA("ja", "Japonés"),
    AR("ar", "Árabe"),
    HI("hi", "Hindi"),
    KO("ko", "Coreano"),
    SV("sv", "Sueco"),
    NO("no", "Noruego"),
    DA("da", "Danés"),
    FI("fi", "Finlandés"),
    IS("is", "Islandés"),
    CS("cs", "Checo"),
    PL("pl", "Polaco"),
    TR("tr", "Turco"),
    EL("el", "Griego"),
    HE("he", "Hebreo"),
    PT_BR("pt-br", "Portugués (Brasil)"),
    RO("ro", "Rumano"),
    HU("hu", "Húngaro"),
    SR("sr", "Serbio"),
    SK("sk", "Eslovaco"),
    BG("bg", "Búlgaro"),
    MS("ms", "Malayo"),
    SW("sw", "Suajili"),
    VI("vi", "Vietnamita"),
    TH("th", "Tailandés"),
    ID("id", "Indonesio"),
    CA("ca", "Catalán"),
    GL("gl", "Gallego"),
    EU("eu", "Vasco"),
    SQ("sq", "Albanés"),
    MK("mk", "Macedonio"),
    LT("lt", "Lituano"),
    LV("lv", "Letón"),
    ET("et", "Estonio"),
    AM("am", "Amárico"),
    FA("fa", "Persa"),
    UR("ur", "Urdu"),
    MY("my", "Birmano"),
    KN("kn", "Kannada"),
    TE("te", "Telugu"),
    ML("ml", "Malayalam"),
    TA("ta", "Tamil"),
    SI("si", "Cingalés"),
    BE("be", "Bielorruso"),
    UK("uk", "Ucraniano"),
    BS("bs", "Bosnio"),
    HR("hr", "Croata"),
    SL("sl", "Esloveno"),
    ;

    private final String codigo;
    private final String nombre;

    Idioma(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public static String obtenerNombrePorCodigo(String codigo) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.getCodigo().equalsIgnoreCase(codigo)) {
                return idioma.getNombre();
            }
        }
        return "Desconocido";
    }
}
