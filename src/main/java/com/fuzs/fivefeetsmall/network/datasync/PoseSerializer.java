package com.fuzs.fivefeetsmall.network.datasync;

import com.fuzs.fivefeetsmall.entity.Pose;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

@SuppressWarnings("NullableProblems")
public class PoseSerializer {

    public static final DataSerializer<Pose> POSE = new DataSerializer<Pose>() {

        public void write(PacketBuffer buf, Pose value) {

            buf.writeEnumValue(value);
        }

        public Pose read(PacketBuffer buf) {

            return buf.readEnumValue(Pose.class);
        }

        public DataParameter<Pose> createKey(int id) {

            return new DataParameter<Pose>(id, this);
        }

        public Pose copyValue(Pose value) {

            return value;
        }

    };

    static {

        DataSerializers.registerSerializer(POSE);
    }

}
