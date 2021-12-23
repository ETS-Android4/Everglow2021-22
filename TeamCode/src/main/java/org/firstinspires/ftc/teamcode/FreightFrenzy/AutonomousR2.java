package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Carrouselle;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Crate;

@Autonomous(name = "R2", group = "Autonomous")
public class AutonomousR2 extends LinearOpMode {

    private DetectionSystem detectionSystem;
    private Crate cratePath;

    @Override
    public void runOpMode() {
        detectionSystem = new DetectionSystem(this);
        cratePath = new Crate(this);
        waitForStart();
        ArmSystem.Floors targetFloor = detectionSystem.findTargetFloor2();
        cratePath.R2(targetFloor);
    }
}
