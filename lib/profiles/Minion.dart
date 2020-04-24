import 'package:flutter/material.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

import 'utils/Creature.dart';

class Minion extends Editable with Creature{

  int woundThreshInd;
  int minionNum;
  List<Item> savedInv;
  List<Weapon> savedWeapons;

  int get cardNum => 9;
  String get fileExtension => ".swminion";

  Minion({@required int id, String name}) : super(id: id, name: name);

  Minion.fromJson(Map<String, dynamic> json) : super.fromJson(json){
    this.creatureLoadJson(json);
    this.woundThreshInd = json["wound threshold per minion"];
    this.minionNum = json["minion number"];
    Map<String,dynamic> saved = json["Saved"];
    this.savedInv = new List();
    for(dynamic d in saved["Inventory"])
      this.savedInv.add(d);
    this.savedWeapons = new List();
    for(dynamic d in saved["Weapons"])
      this.savedWeapons.add(d);
  }

  Minion.load(String filename) : super.load(filename);

  Map<String,dynamic> toJson(){
    var map = super.toJson();
    map.addAll(creatureSaveJson());
    map["wound threshold per minion"] = woundThreshInd;
    map["minion number"] = minionNum;
    var savedInvJson = new List();
    savedInv.forEach((element) {savedInvJson.add(element.toJson());});
    var savedWeapJson = new List();
    savedWeapons.forEach((element) {savedWeapJson.add(element.toJson());});
    map["Saved"] = {
      "Inventory" : savedInvJson,
      "Weapons" : savedWeapJson
    };
    return map;
  }

  List<Widget> cardContents() {
    return List.filled(cardNum,Text("Card"));
  }
}