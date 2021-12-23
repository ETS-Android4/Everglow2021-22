package org.firstinspires.ftc.teamcode.FreightFrenzy;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Carrouselle;

@Autonomous(name = "L2RED", group = "Linear Opmode")
public class L2RED extends LinearOpMode {
    Carrouselle carrouselle;

    @Override
    public void runOpMode() {
        carrouselle = new Carrouselle(this);
        boolean toggle = false;
        waitForStart();
        while (opModeIsActive()){
            carrouselle.L2();
        }
    }
}


