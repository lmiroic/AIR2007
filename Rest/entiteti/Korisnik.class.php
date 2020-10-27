<?php
class Korisnik
{
    public $id;
    public $ime;
    public $prezime;
    public $email;
    public $lozinka;
    public $google_ID;
    
    function __construct($redak,$identifikator=false)
    {
        if($identifikator){
            $this->id=$redak["id"];
        }
        $this->ime=$redak["ime"];
        $this->prezime=$redak["prezime"];
        $this->email=$redak["email"];
        $this->lozinka=$redak["lozinka"];
        $this->google_ID=$redak["google_ID"];
    }
    function getJson()
    {
        if($this->id!=""){
            return array("id"=>$this->id,"ime"=>$this->ime,"prezime"=>$this->prezime,"email"=>$this->email,"lozinka"=>$this->lozinka,"google_ID"=>$this->google_ID);
        }
        else{
            return array("ime"=>$this->ime,"prezime"=>$this->prezime,"email"=>$this->email,"lozinka"=>$this->lozinka,"google_ID"=>$this->google_ID);
        }
        
    }
}
?>