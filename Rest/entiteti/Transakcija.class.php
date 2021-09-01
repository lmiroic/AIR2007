<?php
class Transakcija{
    public $id;
    public $iznos;
    public $datum;
    public $racunTerecenja;
    public $racunPrijenosa;
    public $tipTransakcije;
    public $memo;
    public $opis;
    public $ponavljajuciTrosak;
    public $ikona;
    public $korisnik;
    public $intervalPonavljanja;
    public $kategorijaTransakcije;
    public $placenTrosak;

    function __construct($redak,$identifikator=false){
        if($identifikator){
            $this->id=$redak["id"];
        }        
        $this->iznos=$redak["iznos"];
        $this->datum=$redak["datum"];
        $this->racunTerecenja=$redak["racunTerecenja"];
        $this->racunPrijenosa=$redak["racunPrijenosa"];
        $this->tipTransakcije=$redak["tipTransakcije"];
        $this->memo=$redak["memo"];
        $this->opis=$redak["opis"];
        $this->ponavljajuciTrosak=$redak["ponavljajuciTrosak"];
        $this->ikona=$redak["ikona"];
        $this->korisnik=$redak["korisnik"];
        $this->intervalPonavljanja=$redak["intervalPonavljanja"];
        $this->kategorijaTransakcije=$redak["kategorijaTransakcije"];
        $this->placenTrosak=$redak["placenTrosak"];
    }
    function getJson()
    {        
        if($this->id!=""){
            return array("id"=>$this->id,"iznos"=>$this->iznos,"datum"=>$this->datum,"racunTerecenja"=>$this->racunTerecenja,"racunPrijenosa"=>$this->racunPrijenosa,"tipTransakcije"=>$this->tipTransakcije,"memo"=>$this->memo,"opis"=>$this->opis,"ponavljajuciTrosak"=>$this->ponavljajuciTrosak,"ikona"=>$this->ikona,"korisnik"=>$this->korisnik,"intervalPonavljanja"=>$this->intervalPonavljanja,"kategorijaTransakcije"=>$this->kategorijaTransakcije,"placenTrosak"=>$this->placenTrosak);
        }
        else{
            return array("iznos"=>$this->iznos,"datum"=>$this->datum,"racunTerecenja"=>$this->racunTerecenja,"racunPrijenosa"=>$this->racunPrijenosa,"tipTransakcije"=>$this->tipTransakcije,"memo"=>$this->memo,"opis"=>$this->opis,"ponavljajuciTrosak"=>$this->ponavljajuciTrosak,"ikona"=>$this->ikona,"korisnik"=>$this->korisnik,"intervalPonavljanja"=>$this->intervalPonavljanja,"kategorijaTransakcije"=>$this->kategorijaTransakcije,"placenTrosak"=>$this->placenTrosak);
        }        
    }
}
?>