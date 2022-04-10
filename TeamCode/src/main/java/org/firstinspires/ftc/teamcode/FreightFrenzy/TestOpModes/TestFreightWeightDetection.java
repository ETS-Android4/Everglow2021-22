package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;

@TeleOp(name = "TestFreightWeightDetection", group = "Test")
@Disabled
public class TestFreightWeightDetection extends LinearOpMode {
    EverglowGamepad ourGamepad1;
    ArmSystem armSystem;


    @Override
    public void runOpMode() {
        armSystem = new ArmSystem(this);
        ourGamepad1   = new EverglowGamepad(gamepad1);

        boolean sampleCurr = false;
        int currSum = 0;
        int sampleAmount = 0;

        waitForStart();

        while (opModeIsActive()) {
            ourGamepad1.update();

            if (ourGamepad1.x()) {
                armSystem.reload();
            }

            if (ourGamepad1.y()) {
                armSystem.moveArm(ArmSystem.Floors.THIRD);
                sampleCurr = true;
                sampleAmount = 0;
                currSum = 0;
            }

            if (ourGamepad1.rt()) {
                armSystem.toggleCollecting();
            }

            if(sampleCurr){
                currSum += armSystem.arm.getCurrent(CurrentUnit.MILLIAMPS);
                sampleAmount++;
            }

            if(Math.abs(armSystem.arm.getTargetPosition()-armSystem.arm.getCurrentPosition()) < 20){
                sampleCurr = false;
                if(sampleAmount != 0) {
                    String weightType = "";
                    if(currSum / sampleAmount < 1650){
                        weightType = "none";
                    }
                    else if(currSum / sampleAmount < 1740){
                        weightType = "small";
                    }
                    else if(currSum / sampleAmount < 1810){
                        weightType = "mid";
                    }
                    else{
                        weightType = "big";
                    }
                    telemetry.addData("avrageCurr", currSum/sampleAmount);
                    telemetry.addData("weightType", weightType);
                    telemetry.update();
                }
            }



        }
    }
}
