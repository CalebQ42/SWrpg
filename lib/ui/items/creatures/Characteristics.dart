import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/ui/dialogs/SWDiceDialog.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class Characteristics extends StatelessWidget{

  final bool editing;
  final EditableContentState state;

  Characteristics({required this.editing, required this.state});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            charBuilder(0, Theme.of(context).textTheme.headline6!, context),
            charBuilder(1, Theme.of(context).textTheme.headline6!, context)
          ],
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            charBuilder(2, Theme.of(context).textTheme.headline6!, context),
            charBuilder(3, Theme.of(context).textTheme.headline6!, context)
          ],
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            charBuilder(4, Theme.of(context).textTheme.headline6!, context),
            charBuilder(5, Theme.of(context).textTheme.headline6!, context)
          ],
        ),
      ],
    );
  }
  //0-Brawn,1-Agility,2-Intellect,3-Cunning,4-Willpower,5-Presence
  Widget charBuilder(int charNum, TextStyle style, BuildContext context){
    var creature = Creature.of(context);
    if (creature == null)
      throw "Characteristics card used on non Creature";
    return Expanded(
      child: InkResponse(
        containedInkWell: true,
        onTap:(){
          showModalBottomSheet(
            context: context,
            builder: (context) =>
              SWDiceDialog(
                holder: SWDiceHolder(ability:creature.charVals[charNum]),
                context: context
              )
          );
        },
        child: EditingText(
          editing: editing,
          initialText: creature.charVals[charNum].toString(),
          state: state,
          controller: (){
            var controller = TextEditingController(text: creature.charVals[charNum].toString());
            controller.addListener(() =>
              creature.charVals[charNum] = int.tryParse(controller.text) ?? 0
            );
            return controller;
          }(),
          fieldAlign: TextAlign.center,
          style: style,
          textType: TextInputType.number,
          defaultSave: true,
          title: Creature.characteristics[charNum]
        )
      )
    );
  }
}