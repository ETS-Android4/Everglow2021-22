package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Carousel;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;

@TeleOp(name = "TestPickupTotem", group = "LinearOpMode")
public class TestPickupTotem extends LinearOpMode {

    @Override
    public void runOpMode() {
        DrivingSystem drivingSystem = new DrivingSystem(this);
        ArmSystem armSystem = new ArmSystem(this);
        DetectionSystem detectionSystem = new DetectionSystem(this, armSystem);
        TotemSystem totemSystem = new TotemSystem(this, false);
        waitForStart();

        while (opModeIsActive()) {
            ArmSystem.Floors floor =  detectionSystem.findTargetFloor2(1);
            totemSystem.collectTotem(floor);
            drivingSystem.driveStraight(40, -0.5);
            totemSystem.RestTotem(floor);
            stop();
        }
    }
}
