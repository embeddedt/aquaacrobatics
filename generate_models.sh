#!/bin/bash

declare -a coral_variants=("brain" "bubble" "fire" "horn" "tube")

rm -rf src/generated/resources/assets
mkdir -p src/generated/resources/assets/aquaacrobatics/{blockstates,models/block,models/item}
cd src/generated/resources/assets/aquaacrobatics

coral_block_make_block_model() {
  modelcontent=$(cat <<-EOF
  {
      "parent": "block/cube_all",
      "textures": {
          "all": "aquaacrobatics:block/${1}_coral_block"
      }
  }
EOF
)
  echo $modelcontent > models/block/${1}_coral_block.json
}

coral_make_block_model() {
  modelcontent=$(cat <<-EOF
  {
      "parent": "block/cross",
      "textures": {
          "cross": "aquaacrobatics:block/${1}_coral"
      }
  }
EOF
)
  echo $modelcontent > models/block/${1}_coral.json
}

coral_fan_make_block_model() {
  # NORMAL FAN
  modelcontent=$(cat <<-EOF
    {
        "parent": "aquaacrobatics:block/coral_fan",
        "textures": {
            "fan": "aquaacrobatics:block/${1}_coral_fan"
        }
    }
EOF
)
    echo $modelcontent > models/block/${1}_coral_fan.json
    # FAN ITEM
    modelcontent=$(cat <<-EOF
    {
        "parent": "item/generated",
        "textures": {
            "layer0": "aquaacrobatics:block/${1}_coral_fan"
        }
    }
EOF
)
    echo $modelcontent > models/item/${1}_coral_fan.json
}

coral_wall_fan_make_block_model() {
    # WALL FAN
    modelcontent=$(cat <<-EOF
    {
        "parent": "aquaacrobatics:block/coral_wall_fan",
        "textures": {
            "fan": "aquaacrobatics:block/${1}_coral_fan"
        }
    }
EOF
)
    echo $modelcontent > models/block/${1}_coral_wall_fan.json
}


generate_blockstate() {
    if [ "$2" != "coral_wall_fan" ]; then
        blockstatecontent=$(cat <<-EOF
{
  "forge_marker": 1,
  "defaults": {
    "model": "aquaacrobatics:${1}_${2}"
  },
  "variants": {
    "normal": [{}],
    "inventory": [{}]
  }
}
EOF
)
    else
        blockstatecontent=$(cat <<-EOF
        {
          "variants": {
            "facing=east": { "model": "aquaacrobatics:${1}_${2}", "y": 90 },
            "facing=south": { "model": "aquaacrobatics:${1}_${2}", "y": 180 },
            "facing=west": { "model": "aquaacrobatics:${1}_${2}", "y": 270 },
            "facing=north": { "model": "aquaacrobatics:${1}_${2}", "y": 0 }
          }
        }
EOF
)

    fi
    echo $blockstatecontent > blockstates/${1}_${2}.json
}

for i in "${coral_variants[@]}"
do
   declare -a suffixes=("coral_block" "coral" "coral_fan" "coral_wall_fan")
   normal_name="$i"
   dead_name="dead_$i"
   for suffix in "${suffixes[@]}"
   do
       generate_blockstate ${normal_name} ${suffix}
       generate_blockstate ${dead_name} ${suffix}
       "${suffix}_make_block_model" ${normal_name}
       "${suffix}_make_block_model" ${dead_name}
   done
done