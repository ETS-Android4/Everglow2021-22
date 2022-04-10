package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

@TeleOp(name = "TestPickupTotemRed", group = "LinearOpMode")
@Disabled
public class TestPickupTotemRed extends LinearOpMode {

    @Override
    public void runOpMode() {
        DrivingSystem drivingSystem = new DrivingSystem(this);
        ArmSystem armSystem = new ArmSystem(this);
        DetectionSystem detectionSystem = new DetectionSystem(this, armSystem);
        TotemSystem totemSystem = new TotemSystem(this, false);
        waitForStart();

        while (opModeIsActive()) {
            int mirror = 1;
            totemSystem.prePickupMove(mirror);
            ArmSystem.Floors floor =  detectionSystem.findTargetFloor2(mirror);
            totemSystem.collectTotem(floor, mirror);
            TimeUtils.sleep(5000);
//            drivingSystem.driveStraight(100, -0.7);
            stop();
        }
    }
}
