<?php
class KorisnikovaValuta{
    public $id;
    public $valuta;
    public $korisnik;

    function __construct($redak,$identifikator=false){
        if($identifikator){
            $this->id=$redak["id"];
        }        
        $this->valuta=$redak["valuta"];
        $this->korisnik=$redak["korisnik"];
    }
    function getJson()
    {        
        if($this->id!=""){
            return array("id"=>$this->id,"valuta"=>$this->valuta,"korisnik"=>$this->korisnik);
        }
        else{
            return array("valuta"=>$this->valuta,"korisnik"=>$this->korisnik);
        }
        
    }
}
?>