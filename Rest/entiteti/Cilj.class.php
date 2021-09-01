<?php
class Cilj
{
    public $id;
    public $naziv;
    public $iznos;
    public $korisnik;
    public $datum;
    public $kategorija;
    public $ostvareniCilj;
    
    function __construct($redak,$identifikator=false)
    {
        if($identifikator){
            $this->id=$redak["id"];
        }
        $this->naziv=$redak["naziv"];
        $this->iznos=$redak["iznos"];
        $this->korisnik=$redak["korisnik"];
        $this->datum=$redak["datum"];
        $this->kategorija=$redak["kategorija"];
        $this->ostvareniCilj=$redak["ostvareniCilj"];
    }
    function getJson()
    {
        if($this->id!=""){
            return array("id"=>$this->id,"naziv"=>$this->naziv,"iznos"=>$this->iznos,"korisnik"=>$this->korisnik,"datum"=>$this->datum,"kategorija"=>$this->kategorija,"ostvareniCilj"=>$this->ostvareniCilj);
        }
        else{
            return array("naziv"=>$this->naziv,"iznos"=>$this->iznos,"korisnik"=>$this->korisnik,"datum"=>$this->datum,"kategorija"=>$this->kategorija,"ostvareniCilj"=>$this->ostvareniCilj);
        }
        
    }
}
?>