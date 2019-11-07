package com.crowsofwar.avatar.client.render.komodorhino;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * KomodoRhino Tier1 - talhanation
 * Created using Tabula 5.1.0
 */
public class ModelKomodoRhinoWoven extends ModelKomodoRhino {
    public ModelRenderer saddle25;
    public ModelRenderer shape26;
    public ModelRenderer shape27;
    public ModelRenderer Shape28;
    public ModelRenderer Shape21;
    public ModelRenderer Shape20;
    public ModelRenderer shape29;
    public ModelRenderer shape30;
    public ModelRenderer shape31;
    public ModelRenderer shape32;
    public ModelRenderer rlegfornt;
    public ModelRenderer neck;
    public ModelRenderer rlegback;
    public ModelRenderer llegfront;
    public ModelRenderer llegback;
    public ModelRenderer tail4;
    public ModelRenderer Shape1;
    public ModelRenderer Shape2;
    public ModelRenderer body4;
    public ModelRenderer body5;
    public ModelRenderer Shape3;
    public ModelRenderer Shape4;
    public ModelRenderer body6;
    public ModelRenderer Shape5;
    public ModelRenderer body2;
    public ModelRenderer Shape6;
    public ModelRenderer body3;
    public ModelRenderer Shape7;
    public ModelRenderer tail1;
    public ModelRenderer Shape8;
    public ModelRenderer tail2;
    public ModelRenderer Shape9;
    public ModelRenderer tail3;
    public ModelRenderer body1;
    public ModelRenderer Shape10;
    public ModelRenderer tail5;
    public ModelRenderer Shape11;
    public ModelRenderer Shape12;
    public ModelRenderer Shape13;
    public ModelRenderer Shape14;
    public ModelRenderer Shape21_1;
    public ModelRenderer Shape16;
    public ModelRenderer Shape17;
    public ModelRenderer Shape19;
    public ModelRenderer Shape18;
    public ModelRenderer head;
    public ModelRenderer rshinfront;
    public ModelRenderer rfootfront;
    public ModelRenderer rshinback;
    public ModelRenderer rfootback;
    public ModelRenderer lfootfront;
    public ModelRenderer lshinfront;
    public ModelRenderer lshinback;
    public ModelRenderer lfootback;
    public ModelRenderer horn1r;
    public ModelRenderer horn3;
    public ModelRenderer horn2;
    public ModelRenderer horn1;
    public ModelRenderer horn2r;
    public ModelRenderer horn1l;
    public ModelRenderer horn2l;

    public ModelKomodoRhinoWoven() {
        this.textureWidth = 256;
        this.textureHeight = 128;
        this.body6 = new ModelRenderer(this, 1, 37);
        this.body6.setRotationPoint(-5.0F, 12.0F, 5.0F);
        this.body6.addBox(0.0F, -1.5F, -0.2F, 10, 6, 7, 0.0F);
        this.setRotateAngle(body6, 0.7853981633974483F, -0.0F, 0.0F);
        this.tail3 = new ModelRenderer(this, 0, 0);
        this.tail3.setRotationPoint(-5.0F, 5.0F, 23.5F);
        this.tail3.addBox(0.0F, 0.0F, 0.0F, 10, 9, 12, 0.0F);
        this.setRotateAngle(tail3, -0.471238911151886F, -0.0F, 0.0F);
        this.lfootfront = new ModelRenderer(this, 200, 115);
        this.lfootfront.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lfootfront.addBox(2.0F, 11.0F, -3.5F, 5, 2, 7, 0.0F);
        this.setRotateAngle(lfootfront, -0.3839724354387525F, 0.06981317007977318F, 0.22689280275926282F);
        this.Shape17 = new ModelRenderer(this, 107, 45);
        this.Shape17.setRotationPoint(9.2F, 6.0F, 0.0F);
        this.Shape17.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(Shape17, -0.4363323129985824F, -0.0F, 0.0F);
        this.lshinback = new ModelRenderer(this, 0, 0);
        this.lshinback.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lshinback.addBox(-0.4F, 1.5F, -5.5F, 4, 11, 5, 0.0F);
        this.setRotateAngle(lshinback, 0.593411945678072F, -0.0F, 0.0F);
        this.shape30 = new ModelRenderer(this, 159, 60);
        this.shape30.setRotationPoint(9.1F, 1.0F, -6.8F);
        this.shape30.addBox(-1.0F, 0.0F, -1.0F, 1, 4, 24, 0.0F);
        this.horn1r = new ModelRenderer(this, 48, 29);
        this.horn1r.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.horn1r.addBox(-3.3F, -7.4F, -21.0F, 2, 2, 8, 0.0F);
        this.setRotateAngle(horn1r, -1.0471975511965976F, -0.0F, 0.0F);
        this.Shape4 = new ModelRenderer(this, 98, 30);
        this.Shape4.setRotationPoint(-7.0F, -3.0F, 9.5F);
        this.Shape4.addBox(0.0F, 0.0F, -1.0F, 14, 1, 1, 0.0F);
        this.setRotateAngle(Shape4, 1.8325957145940461F, -0.0F, 0.0F);
        this.Shape28 = new ModelRenderer(this, 92, 48);
        this.Shape28.setRotationPoint(8.2F, -0.5F, 4.0F);
        this.Shape28.addBox(0.0F, 0.0F, 0.0F, 1, 13, 2, 0.0F);
        this.tail2 = new ModelRenderer(this, 0, 0);
        this.tail2.setRotationPoint(-4.5F, 11.0F, 33.5F);
        this.tail2.addBox(0.0F, 0.0F, 0.0F, 9, 8, 12, 0.0F);
        this.setRotateAngle(tail2, -0.43633231520652765F, -0.0F, 0.0F);
        this.Shape7 = new ModelRenderer(this, 74, 45);
        this.Shape7.setRotationPoint(1.8F, -3.0F, 11.5F);
        this.Shape7.addBox(0.0F, 0.0F, -1.0F, 4, 3, 3, 0.0F);
        this.rlegback = new ModelRenderer(this, 0, 0);
        this.rlegback.setRotationPoint(-10.0F, 10.0F, 13.0F);
        this.rlegback.addBox(-1.0F, -4.5F, -4.0F, 5, 9, 8, 0.0F);
        this.setRotateAngle(rlegback, -0.3839724354387525F, -0.0F, 0.22689280275926282F);
        this.Shape11 = new ModelRenderer(this, 92, 48);
        this.Shape11.setRotationPoint(-9.2F, 12.5F, 4.0F);
        this.Shape11.addBox(0.0F, 0.0F, 0.0F, 1, 4, 2, 0.0F);
        this.setRotateAngle(Shape11, 0.0F, -0.0F, -0.460766922526503F);
        this.lfootback = new ModelRenderer(this, 200, 115);
        this.lfootback.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lfootback.addBox(1.7F, 11.5F, -4.4F, 5, 2, 7, 0.0F);
        this.setRotateAngle(lfootback, 0.3839724354387525F, 0.0F, 0.22689280275926282F);
        this.Shape12 = new ModelRenderer(this, 92, 48);
        this.Shape12.setRotationPoint(-9.2F, -0.5F, 4.0F);
        this.Shape12.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1, 0.0F);
        this.setRotateAngle(Shape12, -0.4363323129985824F, -0.0F, 0.0F);
        this.Shape5 = new ModelRenderer(this, 100, 29);
        this.Shape5.setRotationPoint(-2.5F, -2.5F, -2.0F);
        this.Shape5.addBox(0.0F, 0.0F, -1.0F, 6, 1, 1, 0.0F);
        this.horn2 = new ModelRenderer(this, 51, 29);
        this.horn2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.horn2.addBox(-1.0F, 6.1F, -33.3F, 2, 2, 8, 0.0F);
        this.setRotateAngle(horn2, -1.186823891356144F, -0.0F, 0.0F);
        this.Shape6 = new ModelRenderer(this, 74, 45);
        this.Shape6.setRotationPoint(-2.1F, 0.0F, 11.5F);
        this.Shape6.addBox(0.0F, 0.0F, -1.0F, 4, 3, 3, 0.0F);
        this.setRotateAngle(Shape6, 0.0F, -0.0F, -3.141592653589793F);
        this.rlegfornt = new ModelRenderer(this, 0, 0);
        this.rlegfornt.setRotationPoint(-11.0F, 10.0F, -6.0F);
        this.rlegfornt.addBox(-1.0F, -4.5F, -4.0F, 5, 9, 8, 0.0F);
        this.setRotateAngle(rlegfornt, 0.3839724354387525F, -0.0F, 0.22689280275926282F);
        this.Shape1 = new ModelRenderer(this, 88, 29);
        this.Shape1.setRotationPoint(-6.900000095367432F, -0.5F, -2.0F);
        this.Shape1.addBox(0.0F, 0.0F, -1.0F, 7, 1, 12, 0.0F);
        this.shape29 = new ModelRenderer(this, 62, 100);
        this.shape29.setRotationPoint(-8.5F, 0.2F, -6.8F);
        this.shape29.addBox(0.0F, 0.0F, -1.0F, 1, 1, 24, 0.0F);
        this.setRotateAngle(shape29, 0.0F, 0.0F, 0.714712328691678F);
        this.Shape8 = new ModelRenderer(this, 77, 33);
        this.Shape8.setRotationPoint(-2.2F, -3.0F, 11.5F);
        this.Shape8.addBox(0.0F, 0.0F, -1.0F, 4, 3, 3, 0.0F);
        this.head = new ModelRenderer(this, 210, 0);
        this.head.setRotationPoint(0.0F, 4.0F, -9.0F);
        this.head.addBox(-3.5F, -15.0F, -15.0F, 7, 10, 15, 0.0F);
        this.setRotateAngle(head, 1.0471975511965976F, 0.0F, 0.0F);
        this.rfootfront = new ModelRenderer(this, 200, 115);
        this.rfootfront.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rfootfront.addBox(-4.0F, 12.0F, -3.5F, 5, 2, 7, 0.0F);
        this.setRotateAngle(rfootfront, -0.3839724354387525F, -0.06981317007977318F, -0.22689280275926282F);
        this.horn2r = new ModelRenderer(this, 51, 31);
        this.horn2r.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.horn2r.addBox(-2.8F, -7.2F, -27.0F, 1, 1, 6, 0.0F);
        this.setRotateAngle(horn2r, -1.0471975511965976F, -0.0F, 0.0F);
        this.neck = new ModelRenderer(this, 0, 0);
        this.neck.setRotationPoint(0.0F, 0.0F, -0.0F);
        this.neck.addBox(-5.5F, -1.1F, -18.5F, 11, 10, 11, 0.0F);
        this.setRotateAngle(neck, 0.2617993877991494F, -0.0F, 0.0F);
        this.body3 = new ModelRenderer(this, 0, 0);
        this.body3.setRotationPoint(-8.5F, 0.5F, 8.5F);
        this.body3.addBox(0.0F, 0.0F, 0.0F, 17, 10, 9, 0.0F);
        this.setRotateAngle(body3, -0.08726646006107329F, -0.0F, 0.0F);
        this.horn2l = new ModelRenderer(this, 50, 31);
        this.horn2l.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.horn2l.addBox(1.6F, -7.2F, -27.0F, 1, 1, 6, 0.0F);
        this.setRotateAngle(horn2l, -1.0471975511965976F, -0.0F, 0.0F);
        this.horn3 = new ModelRenderer(this, 52, 31);
        this.horn3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.horn3.addBox(-0.5F, 9.6F, -37.0F, 1, 1, 5, 0.0F);
        this.setRotateAngle(horn3, -1.2915436464758039F, -0.0F, 0.0F);
        this.Shape13 = new ModelRenderer(this, 92, 48);
        this.Shape13.setRotationPoint(8.2F, 12.05F, 4.0F);
        this.Shape13.addBox(0.0F, 0.0F, 0.0F, 1, 4, 2, 0.0F);
        this.setRotateAngle(Shape13, 0.0F, -0.0F, 0.41887902047863906F);
        this.body1 = new ModelRenderer(this, 0, 0);
        this.body1.setRotationPoint(-8.5F, 1.0F, -9.5F);
        this.body1.addBox(0.0F, 0.0F, 0.0F, 17, 11, 9, 0.0F);
        this.setRotateAngle(body1, 0.08726646006107329F, -0.0F, 0.0F);
        this.Shape18 = new ModelRenderer(this, 107, 45);
        this.Shape18.setRotationPoint(-10.0F, 6.0F, 0.0F);
        this.Shape18.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(Shape18, -0.4363323129985824F, -0.0F, 0.0F);
        this.shape26 = new ModelRenderer(this, 0, 95);
        this.shape26.setRotationPoint(0.0F, 0.2F, -6.8F);
        this.shape26.addBox(-8.5F, 0.0F, -1.0F, 17, 1, 24, 0.0F);
        this.saddle25 = new ModelRenderer(this, 28, 69);
        this.saddle25.setRotationPoint(9.1F, 5.0F, -6.8F);
        this.saddle25.addBox(-1.0F, 0.0F, 5.0F, 1, 4, 11, 0.0F);
        this.llegback = new ModelRenderer(this, 0, 0);
        this.llegback.setRotationPoint(7.0F, 10.5F, 13.0F);
        this.llegback.addBox(-1.0F, -4.5F, -4.0F, 5, 9, 8, 0.0F);
        this.setRotateAngle(llegback, -0.3839724354387525F, 0.0F, -0.22689280275926282F);
        this.rfootback = new ModelRenderer(this, 200, 115);
        this.rfootback.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rfootback.addBox(-3.5F, 12.0F, -5.0F, 5, 2, 7, 0.0F);
        this.setRotateAngle(rfootback, 0.3839724354387525F, -0.0F, -0.22689280275926282F);
        this.shape32 = new ModelRenderer(this, 104, 60);
        this.shape32.setRotationPoint(-9.1F, 0.9F, -6.8F);
        this.shape32.addBox(0.0F, 0.0F, -1.0F, 1, 4, 24, 0.0F);
        this.shape27 = new ModelRenderer(this, 115, 100);
        this.shape27.setRotationPoint(8.5F, 0.2F, -6.8F);
        this.shape27.addBox(0.0F, 0.0F, -1.0F, 1, 1, 24, 0.0F);
        this.setRotateAngle(shape27, 0.0F, 0.0F, 0.8726646259971648F);
        this.body2 = new ModelRenderer(this, 0, 0);
        this.body2.setRotationPoint(-8.0F, 0.5F, -1.0F);
        this.body2.addBox(0.0F, 0.0F, 0.0F, 16, 11, 10, 0.0F);
        this.Shape2 = new ModelRenderer(this, 88, 29);
        this.Shape2.setRotationPoint(0.0F, -0.5F, -2.0F);
        this.Shape2.addBox(0.0F, 0.0F, -1.0F, 7, 1, 12, 0.0F);
        this.body4 = new ModelRenderer(this, 1, 51);
        this.body4.setRotationPoint(-5.0F, 8.0F, -6.0F);
        this.body4.addBox(0.0F, -0.8F, -1.9F, 10, 6, 7, 0.0F);
        this.setRotateAngle(body4, -0.7853981633974483F, -0.0F, 0.0F);
        this.Shape10 = new ModelRenderer(this, 100, 29);
        this.Shape10.setRotationPoint(-7.0F, -1.5F, -2.0F);
        this.Shape10.addBox(0.0F, 0.0F, -1.0F, 14, 1, 1, 0.0F);
        this.Shape19 = new ModelRenderer(this, 92, 48);
        this.Shape19.setRotationPoint(-9.2F, -0.5F, 4.0F);
        this.Shape19.addBox(0.0F, 0.0F, 0.0F, 1, 13, 2, 0.0F);
        this.lshinfront = new ModelRenderer(this, 0, 0);
        this.lshinfront.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lshinfront.addBox(-0.4F, 1.5F, 0.8F, 4, 11, 5, 0.0F);
        this.setRotateAngle(lshinfront, -0.593411945678072F, -0.0F, 0.0F);
        this.tail4 = new ModelRenderer(this, 0, 0);
        this.tail4.setRotationPoint(-4.0F, 18.0F, 41.5F);
        this.tail4.addBox(0.0F, 0.0F, 0.0F, 8, 5, 11, 0.0F);
        this.setRotateAngle(tail4, -0.1047197580337524F, -0.0F, 0.0F);
        this.rshinfront = new ModelRenderer(this, 0, 0);
        this.rshinfront.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rshinfront.addBox(-0.4F, 1.5F, 0.8F, 4, 11, 5, 0.0F);
        this.setRotateAngle(rshinfront, -0.593411945678072F, -0.0F, 0.0F);
        this.horn1 = new ModelRenderer(this, 51, 29);
        this.horn1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.horn1.addBox(-1.2F, 2.4F, -26.4F, 3, 3, 8, 0.0F);
        this.setRotateAngle(horn1, -1.048942880448592F, 0.015533430342749534F, 0.0F);
        this.Shape21_1 = new ModelRenderer(this, 92, 48);
        this.Shape21_1.setRotationPoint(7.3F, -0.4F, 4.0F);
        this.Shape21_1.addBox(-1.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
        this.tail1 = new ModelRenderer(this, 0, 0);
        this.tail1.setRotationPoint(-5.5F, 2.0F, 15.5F);
        this.tail1.addBox(0.0F, 0.0F, 0.0F, 11, 10, 9, 0.0F);
        this.setRotateAngle(tail1, -0.2617993950843811F, -0.0F, 0.0F);
        this.Shape16 = new ModelRenderer(this, 92, 48);
        this.Shape16.setRotationPoint(8.2F, -0.5F, 4.0F);
        this.Shape16.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1, 0.0F);
        this.setRotateAngle(Shape16, -0.4363323129985824F, -0.0F, 0.0F);
        this.Shape20 = new ModelRenderer(this, 92, 48);
        this.Shape20.setRotationPoint(7.3F, -0.4F, 4.0F);
        this.Shape20.addBox(-1.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
        this.shape31 = new ModelRenderer(this, 0, 69);
        this.shape31.setRotationPoint(-9.1F, 4.9F, -6.8F);
        this.shape31.addBox(0.0F, 0.0F, 5.0F, 1, 4, 11, 0.0F);
        this.Shape9 = new ModelRenderer(this, 96, 30);
        this.Shape9.setRotationPoint(-7.0F, 0.0F, 9.0F);
        this.Shape9.addBox(0.0F, 0.0F, -1.0F, 14, 1, 2, 0.0F);
        this.setRotateAngle(Shape9, 1.1344640137963142F, -0.0F, 0.0F);
        this.Shape14 = new ModelRenderer(this, 92, 48);
        this.Shape14.setRotationPoint(6.5F, 15.0F, 4.0F);
        this.Shape14.addBox(0.0F, -1.0F, 0.0F, 1, 15, 2, 0.0F);
        this.setRotateAngle(Shape14, 0.0F, -0.0F, 1.5707963267948966F);
        this.horn1l = new ModelRenderer(this, 49, 30);
        this.horn1l.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.horn1l.addBox(1.3F, -7.4F, -21.0F, 2, 2, 8, 0.0F);
        this.setRotateAngle(horn1l, -1.0471975511965976F, -0.0F, -0.0F);
        this.Shape21 = new ModelRenderer(this, 92, 48);
        this.Shape21.setRotationPoint(-7.3F, -0.4F, 4.0F);
        this.Shape21.addBox(-1.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
        this.Shape3 = new ModelRenderer(this, 96, 30);
        this.Shape3.setRotationPoint(-7.0F, -1.5F, 9.5F);
        this.Shape3.addBox(0.0F, 0.0F, -1.0F, 14, 1, 2, 0.0F);
        this.setRotateAngle(Shape3, 1.413716694115407F, -0.0F, 0.0F);
        this.rshinback = new ModelRenderer(this, 0, 0);
        this.rshinback.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rshinback.addBox(-0.4F, 1.5F, -5.5F, 4, 11, 5, 0.0F);
        this.setRotateAngle(rshinback, 0.593411945678072F, -0.0F, 0.0F);
        this.body5 = new ModelRenderer(this, 36, 43);
        this.body5.setRotationPoint(-5.5F, 8.4F, -6.0F);
        this.body5.addBox(0.0F, 0.0F, 0.0F, 11, 7, 14, 0.0F);
        this.tail5 = new ModelRenderer(this, 0, 0);
        this.tail5.setRotationPoint(-4.0F, 16.53333282470703F, 43.5F);
        this.tail5.addBox(0.0F, 0.0F, 0.0F, 8, 2, 6, 0.0F);
        this.setRotateAngle(tail5, -0.5061454772949219F, -0.0F, 0.0F);
        this.llegfront = new ModelRenderer(this, 0, 0);
        this.llegfront.setRotationPoint(8.0F, 11.0F, -6.0F);
        this.llegfront.addBox(-1.0F, -4.5F, -4.0F, 5, 9, 8, 0.0F);
        this.setRotateAngle(llegfront, 0.3839724354387525F, -0.0F, -0.22689280275926282F);
        this.llegfront.addChild(this.lfootfront);
        this.llegback.addChild(this.lshinback);
        this.head.addChild(this.horn1r);
        this.llegback.addChild(this.lfootback);
        this.head.addChild(this.horn2);
        this.rlegfornt.addChild(this.rfootfront);
        this.head.addChild(this.horn2r);
        this.head.addChild(this.horn2l);
        this.head.addChild(this.horn3);
        this.rlegback.addChild(this.rfootback);
        this.llegfront.addChild(this.lshinfront);
        this.rlegfornt.addChild(this.rshinfront);
        this.head.addChild(this.horn1);
        this.head.addChild(this.horn1l);
        this.rlegback.addChild(this.rshinback);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.body6.render(f5);
        this.tail3.render(f5);
        this.Shape17.render(f5);
        this.shape30.render(f5);
        this.Shape4.render(f5);
        this.Shape28.render(f5);
        this.tail2.render(f5);
        this.Shape7.render(f5);
        this.rlegback.render(f5);
        this.Shape11.render(f5);
        this.Shape12.render(f5);
        this.Shape5.render(f5);
        this.Shape6.render(f5);
        this.rlegfornt.render(f5);
        this.Shape1.render(f5);
        this.shape29.render(f5);
        this.Shape8.render(f5);
        this.head.render(f5);
        this.neck.render(f5);
        this.body3.render(f5);
        this.Shape13.render(f5);
        this.body1.render(f5);
        this.Shape18.render(f5);
        this.shape26.render(f5);
        this.saddle25.render(f5);
        this.llegback.render(f5);
        this.shape32.render(f5);
        this.shape27.render(f5);
        this.body2.render(f5);
        this.Shape2.render(f5);
        this.body4.render(f5);
        this.Shape10.render(f5);
        this.Shape19.render(f5);
        this.tail4.render(f5);
        this.Shape21_1.render(f5);
        this.tail1.render(f5);
        this.Shape16.render(f5);
        this.Shape20.render(f5);
        this.shape31.render(f5);
        this.Shape9.render(f5);
        this.Shape14.render(f5);
        this.Shape21.render(f5);
        this.Shape3.render(f5);
        this.body5.render(f5);
        this.tail5.render(f5);
        this.llegfront.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    protected ModelRenderer getNeck() {
        return neck;
    }

    @Override
    protected ModelRenderer getFrontLeftLeg() {
        return llegfront;
    }

    @Override
    protected ModelRenderer getBackRightLeg() {
        return rlegback;
    }


    @Override
    protected ModelRenderer getFrontRightLeg() {
        return rlegfornt;
    }

    @Override
    protected ModelRenderer getBackLeftLeg() {
        return llegback;
    }

}

