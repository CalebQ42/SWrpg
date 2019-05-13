import 'package:meta/meta.dart';
import 'package:swassistant/items/CriticalInjury.dart';
import 'package:swassistant/items/Note.dart';
import 'package:swassistant/items/Weapon.dart';

import '../Character.dart';
import '../Minion.dart';
import '../Vehicle.dart';
import 'JsonSavable.dart';

//Editable holds all common components of Vehicles, Minions, and Characters and
//provides a framework on how to display, load, and save profiles
abstract class Editable extends JsonSavable{

  //Common components

  int id = 0;
  String name;
  List<Note> nts;
  List<Weapon> weapons;
  String category;
  List<CriticalInjury> criticalInjuries;
  String desc;

  //Saving variables

  bool _editing = false;
  bool _saving = false;
  String _loc;
  bool _external;

  Editable.fromJson(Map<String,dynamic> json){
    //id = json["id"];
    name = json["name"];
    nts = new List<Note>();
    for (Map<String, dynamic> arrMap in json["Notes"]) {
      nts.add(Note.fromJson(arrMap));
    }
    weapons = new List<Weapon>();
    for(Map<String,dynamic> arrMap in json["Weapons"]){
      weapons.add(Weapon.fromJson(arrMap));
    }
    category = json["category"];
    criticalInjuries = new List<CriticalInjury>();
    for(Map<String,dynamic> arrMap in json["Critical Injuries"]){
      criticalInjuries.add(CriticalInjury.fromJson(arrMap));
    }
    desc = json["description"];
  }

  @virtual
  @mustCallSuper
  Map<String, dynamic> toJson(){
    if (!(this is Character || this is Vehicle || this is Minion)){
      throw("Must be overridden by child");
    }
    var noteMap = new List<Map<String,dynamic>>();
    for (Note nt in nts){
      noteMap.add(nt.toJson());
    }
    var weaponMap = new List<Map<String,dynamic>>();
    for (Weapon wp in weapons){
      weaponMap.add(wp.toJson());
    }
    var criticalInjuryMap = new List<Map<String,dynamic>>();
    for (CriticalInjury wp in criticalInjuries){
      criticalInjuryMap.add(wp.toJson());
    }
    return {
      "name" : name,
      "Notes" : noteMap,
      "Weapons" : weaponMap,
      "category" : category,
      "Critical Injuries" : criticalInjuryMap,
      "description" : desc
    };
  }

  void saving(){
    print("Oh howdy yall");
  }
}