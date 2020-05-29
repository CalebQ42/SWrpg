import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:meta/meta.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/items/CriticalInjury.dart';
import 'package:swassistant/items/Note.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/ui/Card.dart';

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

  List<bool> showCard;

  String get fileExtension;
  int get cardNum;

  //Saving variables
  bool _saving = false;
  String _loc;
  bool _defered = false;

  Editable({@required this.id, this.name = "", this.nts, this.weapons, this.category = "", this.criticalInjuries, this.desc = ""}){
    nts ??= new List();
    weapons ??= new List();
    criticalInjuries ??= new List();
    showCard = List.filled(cardNum, false);
  }

  Editable.load(FileSystemEntity file, SW app){
    var jsonMap = jsonDecode(File.fromUri(file.uri).readAsStringSync());
    loadJson(jsonMap);
    if(getFileLocation(app)!= file.path)
      _loc = file.path;
  }

  @mustCallSuper
  void loadJson(Map<String,dynamic> json){
    if (!(this is Character || this is Vehicle || this is Minion))
      throw("Must be overridden by child");
    id = json["id"];
    name = json["name"];
    nts = new List<Note>();
    for (Map<String, dynamic> arrMap in json["Notes"]) 
      nts.add(Note.fromJson(arrMap));
    weapons = new List<Weapon>();
    for(Map<String,dynamic> arrMap in json["Weapons"])
      weapons.add(Weapon.fromJson(arrMap));
    category = json["category"];
    criticalInjuries = new List<CriticalInjury>();
    for(Map<String,dynamic> arrMap in json["Critical Injuries"])
      criticalInjuries.add(CriticalInjury.fromJson(arrMap));
    desc = json["description"];
    showCard = json["show cards"].cast<bool>();
  }

  @mustCallSuper
  Map<String, dynamic> toJson(){
    if (!(this is Character || this is Vehicle || this is Minion))
      throw("Must be overridden by child");
    var json = new Map<String,dynamic>();
    json["Notes"] = List.generate(nts.length, (index) => nts[index].toJson());
    json["Weapons"] = List.generate(weapons.length, (index) => weapons[index].toJson());
    json["Critical Injuries"] = List.generate(criticalInjuries.length, (index) => criticalInjuries[index].toJson());
    json["name"] = name;
    json["category"] = category;
    json["description"] = desc;
    json["show cards"] = showCard;
    return json;
  }

  List<Widget> cards(Function refresh, SW app){
    var cards = List<Widget>();
    var contents = cardContents(app);
    cards.add(Card(
      child: Padding(
        padding: EdgeInsets.all(10.0),
        child: NameCardContent(this, refresh, app)
      )
    ));
    for (int i = 0; i < contents.length; i++){
      cards.add(
        InfoCard(shown: showCard[i],contents: contents[i], title: "Woolooloo", onHideChange: (bool b){showCard[i]=b;})
      );
    }
    return cards;
  }

  List<Widget> cardContents(SW app);

  void exportTo(String folder){}
  String getFileLocation(SW sw){
    if(_loc == null || _loc == "")
      return sw.saveDir+ "/" + id.toString() + fileExtension;
    else
      return _loc;
  }
  String getCloudFileLocation(){return null;}
  void save(String filename) async{
    if(!_saving){
      _saving = true;
      var file = File(filename);
      var backup = file.renameSync(filename +".backup");
      file.createSync();
      file.writeAsStringSync(jsonEncode(toJson()));
      backup.deleteSync();
      _saving = false;
    }else{
      print("defering save");
      if(!_defered){
        _defered = true;
        while(_saving){
          sleep(Duration(milliseconds: 250));
        }
        save(filename);
        _defered = false;
      }
    }
  }

  void cloudSave(){}
  void load(String filename){
    var file = File(filename);
    loadJson(jsonDecode(file.readAsStringSync()));
  }
  void cloudLoad(){}
  void delete(){}

  void addShortcut(){}
  bool hasShortcut(){ return false; }
  void updateShortcut(){}
  void deleteShortcut(){}
}