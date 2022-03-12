package org.firstinspires.ftc.teamcode.FreightFrenzy.Paths;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class SharedPaths {
    DrivingSystem drivingSystem;
    ArmSystem armSystem;
    DuckSystem duckSystem;
    TotemSystem totemSystem;
    DetectionSystem detectionSystem;
    LinearOpMode opMode;

    public SharedPaths(AllSystems allSystems) {
        this.drivingSystem = allSystems.drivingSystem;
        this.armSystem = allSystems.armSystem;
        this.duckSystem = allSystems.duckSystem;
        this.totemSystem = allSystems.totemSystem;
        this.detectionSystem = allSystems.detectionSystem;
        this.opMode = allSystems.opMode;
    }

    public void collectAndPlaceFreight(int i){
//        drivingSystem.driveStraight(70, 0.7);
//        double distance = drivingSystem.driveUntilCollect(70, 0.3);
//        TimeUtils.sleep(200);
//        drivingSystem.driveStraight(distance / 2, -0.6);
//        drivingSystem.driveSideways(5, 0.6);
//        drivingSystem.driveStraight(68 + distance / 2, -0.8);
//        armSystem.moveArm(ArmSystem.Floors.THIRD);
//        drivingSystem.driveToPoint(0, -73, 60 - i * 10, 0.5, 0.5);
//        TimeUtils.sleep(300);
//        armSystem.spit();
//        TimeUtils.sleep(300);
//        armSystem.stop();
//        armSystem.moveArm(0);
//        drivingSystem.driveToPoint(0, 80, 90, 0.5, 0.5);
    }
}
