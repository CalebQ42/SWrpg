import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/Skill.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/dialogs/WeaponCharacteristicDialog.dart';

class WeaponEditDialog extends StatefulWidget{
  final Function(Weapon) onClose;
  final Weapon weapon;
  final Editable editable;

  WeaponEditDialog({this.onClose, Weapon weapon, this.editable}) :
      this.weapon = weapon == null ? Weapon.nulled() : Weapon.from(weapon);

  @override
  State<StatefulWidget> createState() => _WeaponEditDialogState(onClose: onClose, weapon: weapon, editable: editable);
}

class _WeaponEditDialogState extends State{
  final void Function(Weapon) onClose;
  final Weapon weapon;
  final Editable editable;

  TextEditingController nameController;
  TextEditingController damageController;
  TextEditingController criticalController;
  TextEditingController hpController;
  TextEditingController encumbranceController;
  TextEditingController arcController;
  TextEditingController ammoController;

  _WeaponEditDialogState({this.onClose, this.weapon, this.editable}){
    nameController = TextEditingController(text: weapon.name)
        ..addListener(() {
          if((weapon.name == "" && nameController.text != "") || (weapon.name != "" && nameController.text == ""))
            setState(() => weapon.name = nameController.text);
          else
            weapon.name = nameController.text;
        });
    damageController = TextEditingController(text: weapon.damage != null ? weapon.damage.toString() : "")
        ..addListener(() {
          var val = int.tryParse(damageController.text);
          if((weapon.damage == null && val != null) || (weapon.damage != null && val == null))
            setState(() => weapon.damage = val);
          else
            weapon.damage = val;
        });
    criticalController = TextEditingController(text: weapon.critical != null ? weapon.critical.toString() : "")
        ..addListener(() {
          var val = int.tryParse(criticalController.text);
          if((weapon.critical == null && val != null) || (weapon.critical != null && val == null)){
            setState(() => weapon.critical = val);
          }else
            weapon.critical = val;
        });
    hpController = TextEditingController(text: weapon.hp != null ? weapon.hp.toString() : "")
        ..addListener(() {
          var val = int.tryParse(hpController.text);
          if((weapon.hp == null && val != null) || (weapon.hp != null && val == null))
            setState(() => weapon.hp = val);
          else
            weapon.hp = val;
        });
    encumbranceController = TextEditingController(text: weapon.encumbrance != null ? weapon.encumbrance.toString() : "")
        ..addListener(() {
          var val = int.tryParse(encumbranceController.text);
          if((weapon.encumbrance == null && val != null) || (weapon.encumbrance != null && val == null))
            setState(() => weapon.encumbrance = val);
          else
            weapon.encumbrance = val;
        });
    arcController = TextEditingController(text: weapon.firingArc)
      ..addListener(() {
        if((weapon.firingArc == "" && arcController.text != "") || (weapon.firingArc != "" && arcController.text == ""))
          setState(() => weapon.firingArc = arcController.text);
        else
          weapon.firingArc = arcController.text;
      });
    ammoController = TextEditingController(text: weapon.ammo.toString())
        ..addListener(() {
          var val = int.tryParse(ammoController.text);
          if((weapon.ammo == null && val != null) || (weapon.ammo != null && val == null))
            setState(() => weapon.ammo = val);
          else
            weapon.ammo = val;
        });
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: DropdownButtonHideUnderline(
        child: Padding(
          padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15, top: 15)),
          child: Column(
            children: [
              //Name
              TextField(
                controller: nameController,
                decoration: InputDecoration(
                  prefixText: "Name: ",
                  labelText: "Name: ",
                  floatingLabelBehavior: FloatingLabelBehavior.never
                ),
              ),
              Container(height: 10),
              //Damage
              TextField(
                controller: damageController,
                decoration: InputDecoration(
                  prefixText: "Damage: ",
                  labelText: "Damage: ",
                  floatingLabelBehavior: FloatingLabelBehavior.never
                ),
                keyboardType: TextInputType.number,
                inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
              ),
              Container(height: 10),
              //Critical
              TextField(
                controller: criticalController,
                decoration: InputDecoration(
                  prefixText: "Critical: ",
                  labelText: "Critical: ",
                  floatingLabelBehavior: FloatingLabelBehavior.never
                ),
                keyboardType: TextInputType.number,
                inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
              ),
              Container(height: 10),
              //Hard Points
              TextField(
                controller: hpController,
                decoration: InputDecoration(
                  prefixText: "Hard Points: ",
                  labelText: "Hard Points: ",
                  floatingLabelBehavior: FloatingLabelBehavior.never
                ),
                keyboardType: TextInputType.number,
                inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
              ),
              //Encumbrance
              if(editable is Character) Container(height: 10),
              if(editable is Character) TextField(
                controller: encumbranceController,
                decoration: InputDecoration(
                  prefixText: "Encumbrance: ",
                  labelText: "Encumbrance: ",
                  floatingLabelBehavior: FloatingLabelBehavior.never
                ),
                keyboardType: TextInputType.number,
                inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
              ),
              //Firing Arc
              if(editable is Vehicle) Container(height: 10),
              if(editable is Vehicle) TextField(
                controller: arcController,
                decoration: InputDecoration(
                  prefixText: "Firing Arc: ",
                  labelText: "Firing Arc: ",
                  floatingLabelBehavior: FloatingLabelBehavior.never
                ),
              ),
              //Range
              Container(height: 10),
              InputDecorator(
                decoration: InputDecoration(
                  prefixText: "Range: ",
                  labelText: "Range: ",
                  floatingLabelBehavior: FloatingLabelBehavior.never
                ),
                child: DropdownButton<int>(
                  isDense: true,
                  isExpanded: true,
                  hint: Text("Weapon Range"),
                  onTap: () => FocusScope.of(context).unfocus(),
                  items: [
                    DropdownMenuItem(
                      child: Text("Engaged"),
                      value: 0,
                    ),
                    DropdownMenuItem(
                      child: Text("Short"),
                      value: 1,
                    ),
                    DropdownMenuItem(
                      child: Text("Medium"),
                      value: 2,
                    ),
                    DropdownMenuItem(
                      child: Text("Long"),
                      value: 3,
                    ),
                    DropdownMenuItem(
                      child: Text("Extreme"),
                      value: 4,
                    )
                  ],
                  value: weapon.range,
                  onChanged: (value) => setState(()=>weapon.range = value),
                )
              ),
              //Damage
              Container(height: 10),
              InputDecorator(
                decoration: InputDecoration(
                  prefixText: "Item Damage: ",
                  labelText: "Item Damage: ",
                  floatingLabelBehavior: FloatingLabelBehavior.never
                ),
                child: DropdownButton<int>(
                  isDense: true,
                  isExpanded: true,
                  onTap: () => FocusScope.of(context).unfocus(),
                  items: [
                    DropdownMenuItem(
                      child: Text("None"),
                      value: 0,
                    ),
                    DropdownMenuItem(
                      child: Text("Short"),
                      value: 1,
                    ),
                    DropdownMenuItem(
                      child: Text("Medium"),
                      value: 2,
                    ),
                    DropdownMenuItem(
                      child: Text("Long"),
                      value: 3,
                    ),
                  ],
                  value: weapon.itemState,
                  onChanged: (value) => setState(() => weapon.itemState = value),
                )
              ),
              //Skill
              Container(height: 10),
              InputDecorator(
                decoration: InputDecoration(
                  prefixText: "Skill: ",
                  labelText: "Skill: ",
                  floatingLabelBehavior: FloatingLabelBehavior.never
                ),
                child: DropdownButton<int>(
                  isDense: true,
                  isExpanded: true,
                  onTap: () => FocusScope.of(context).unfocus(),
                  items: List.generate(
                    Weapon.weaponSkills.length,
                    (i) => DropdownMenuItem(
                      child: Text(Weapon.weaponSkills[i]),
                      value: i
                    )
                  ),
                  value: weapon.skill,
                  onChanged: (value) => setState((){
                    weapon.skill = value;
                    weapon.skillBase = Skill.skillsList[Weapon.weaponSkills[value]];
                  }),
                  hint: Text("Skill"),
                )
              ),
              //SkillBase
              Container(height: 10),
              InputDecorator(
                decoration: InputDecoration(
                  prefixText: "Skill Base: ",
                  labelText: "Skill Base: ",
                  floatingLabelBehavior: FloatingLabelBehavior.never
                ),
                child: DropdownButton<int>(
                  isDense: true,
                  isExpanded: true,
                  onTap: () => FocusScope.of(context).unfocus(),
                  items: List.generate(
                    Creature.characteristics.length,
                    (i) => DropdownMenuItem(
                      child: Text(Creature.characteristics[i]),
                      value: i
                    )
                  ),
                  value: weapon.skillBase,
                  onChanged: (value) => setState(() => weapon.skillBase = value),
                  hint: Text("Skill Base"),
                )
              ),
              //Characteristics
              Container(height: 15),
              Center(child: Text("Characteristics", style: Theme.of(context).textTheme.headline6),),
              Column(
                children: List.generate(weapon.characteristics.length,(i){
                  return InkResponse(
                    containedInkWell: true,
                    onTap: () => showDialog(
                      context: context,
                      child: WeaponCharacteristicDialog(
                        wc: weapon.characteristics[i],
                        onClose: (wc){
                          if(wc != null)
                            setState(() => weapon.characteristics[i] = wc);
                        }
                      )
                    ),
                    child: Padding(
                      padding: EdgeInsets.all(5),
                      child: Row(
                        children: [
                          Expanded(
                            child: Text(weapon.characteristics[i].name + " " +weapon.characteristics[i].value.toString()),
                          ),
                          Text(weapon.characteristics[i].advantage.toString())
                        ],
                      )
                    )
                  );
                })
              ),
              FlatButton.icon(
                label: Text("Add Characteristic"),
                icon: Icon(Icons.add),
                onPressed: (){
                  showDialog(
                    context: context,
                    child: WeaponCharacteristicDialog(onClose: (wc){
                      if(wc != null)
                        weapon.characteristics.add(wc);
                    })
                  );
                },
              ),
              //Add Brawn
              if(editable is Character || editable is Minion) SwitchListTile(
                value: weapon.addBrawn,
                onChanged: (value) => setState(() => weapon.addBrawn = value),
                title: Text("Add Brawn to Damage"),
              ),
              //Loaded
              SwitchListTile(
                value: weapon.loaded,
                onChanged: (value) => setState(() => weapon.loaded = value),
                title: Text("Loaded")
              ),
              //Limited Ammo
              SwitchListTile(
                value: weapon.limitedAmmo,
                onChanged: (value) => setState((){
                  weapon.limitedAmmo = value;
                  weapon.ammo = 0;
                  if(value)
                    ammoController.text = "0";
                }),
                title: Text("Slugthrower (Needs Ammo)")
              ),
              //Ammo
              if(weapon.limitedAmmo) Container(height: 10),
              AnimatedSwitcher(
                duration: Duration(milliseconds: 300),
                transitionBuilder: (child, anim){
                  return SizeTransition(
                    sizeFactor: anim,
                    child: child
                  );
                },
                child: weapon.limitedAmmo ? TextField(
                  controller: ammoController,
                  decoration: InputDecoration(
                    prefixIcon: Text("Ammo: ", style: TextStyle(color:Theme.of(context).hintColor),),
                    prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
                  ),
                  keyboardType: TextInputType.number,
                  inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
                ) : Container(),
              ),
              ButtonBar(
                children: [
                  FlatButton(
                    child: Text("Cancel"),
                    onPressed: (){
                      Navigator.of(context).pop();
                    },
                  ),
                  FlatButton(
                    child: Text("Save"),
                    onPressed: weapon.name != "" && weapon.damage != null && weapon.critical != null && weapon.hp != null
                        && (editable is Character && weapon.encumbrance != null) && (editable is Vehicle && weapon.firingArc != null)
                        && weapon.range != null && weapon.damage != null && weapon.skill != null && weapon.skillBase != null ? (){
                      onClose(weapon);
                      Navigator.of(context).pop();
                    } : null
                  )
                ],
              )
            ],
          )
        )
      )
    );
  }
}