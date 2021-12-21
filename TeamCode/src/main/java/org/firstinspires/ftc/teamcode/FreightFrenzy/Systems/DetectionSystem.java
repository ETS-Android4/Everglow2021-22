package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class DetectionSystem {
    private final LinearOpMode opMode;

    public DetectionSystem(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public ArmSystem.Floor findTargetFloor(){
        // todo: use actual sensors
        return ArmSystem.Floor.THIRD;
    }
}
