package com.fuzs.aquaacrobatics.network.datasync;

import com.fuzs.aquaacrobatics.entity.Pose;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class PoseSerializer {

    public static final DataSerializer<Pose> POSE = new DataSerializer<Pose>() {

        public void write(PacketBuffer buf, Pose value) {

            buf.writeEnumValue(value);
        }

        public Pose read(PacketBuffer buf) {

            return buf.readEnumValue(Pose.class);
        }

        public DataParameter<Pose> createKey(int id) {

            return new DataParameter<>(id, this);
        }

        public Pose copyValue(Pose value) {

            return value;
        }

    };

    static {

        DataSerializers.registerSerializer(POSE);
    }

}
