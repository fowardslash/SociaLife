package com.example.projectmain.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmain.Database.DB;
import com.example.projectmain.EditPostActivity;
import com.example.projectmain.Fragment.HomeFragment;
import com.example.projectmain.Fragment.UserFragment;
import com.example.projectmain.ImageActivity;
import com.example.projectmain.LikeActivity;
import com.example.projectmain.MainActivity;
import com.example.projectmain.Model.Post;
import com.example.projectmain.Model.User;
import com.example.projectmain.PostDetailActitivty;
import com.example.projectmain.R;
import com.example.projectmain.SettingActivity;
import com.example.projectmain.UserActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {


    public PostAdapter(Context context, List<Post> posts) {
        this.posts = posts;
        this.context = context;
    }

    Context context;
    List<Post> posts;
    List<User> users;
    List<String> listName;
    DB db;
    CheckBox btnLike;
    User user;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_IMAGE_LINK = "linkImage";

    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    String email = null;
    String name = null;

    /*
     * Lấy kiểu view
     * Hàm này được dùng để lấy kiểu view
     * Tham số: position: int: Dùng để lấy vị trí hiện tại đang xét
     * Các kiểu view:
     * 0: Post có hình nhưng ko caption
     * 1: Post có caption ngắn
     * 2: Post có caption dài
     * 3: Post vừa có hình, vừa có caption
     * */
    @Override
    public int getItemViewType(int position) {
        String postContent = posts.get(position).getContent();

        String img = posts.get(position).getImgPost();
        Post childPost = posts.get(position).getSharedPost();

        if (childPost != null) {
            return 5;
        }
        if (postContent == null && !img.equals("null")) {
            return 0;
        }
        if (postContent.length() <= 50 && img.equals("null")) {
            return 1;
        }
        if (postContent.length() >= 50 && img.equals("null")) {
            return 2;
        }
        if (postContent != null && !img.equals("null")) {
            return 3;
        }
        return 4;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        visited = new Boolean[posts.size()];

        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_img_notext, parent, false);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_small_paragraph, parent, false);
        } else if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_large_paragraph, parent, false);
        } else if (viewType == 3) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
        } else if (viewType == 5) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_post, parent, false);
        } else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.error, parent, false);

        return new PostViewHolder(view);
    }

    @SuppressLint({"SuspiciousIndentation", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        db = new DB(context.getApplicationContext());
        Post childPost = posts.get(position).getSharedPost();
        Post post = posts.get(position);

        //   User user = users.get(position);
        int type = getItemViewType(position);
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IMAGE_LINK, post.getImgPost());
        email = sharedPreferences.getString(KEY_EMAIL, null);
        name = sharedPreferences.getString(KEY_NAME, null);

        user = db.getUser(email);
        editor.apply();

        if (post == null)
            return;

        String ava = post.getAvatar();
        if (ava.equals("null")) {
            holder.avatar.setImageResource(R.drawable.def);
        } else
            holder.avatar.setImageURI(Uri.parse(ava));
        //    holder.imgPost.setImageURI(Uri.parse(post.getImgPost()));
        holder.name.setText(post.getName());
        holder.userName.setText(post.getUsername());
        holder.numberLike.setText(String.valueOf(db.getLike(post.getId()).getCount()));
        holder.content.setText(post.getContent());

        Time now = new Time(position);

        holder.time.setText(post.getTime());

        holder.btnShowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UserActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putInt("idUser", post.getIduser());
                i.putExtras(b);
                context.startActivity(i);
            }
        });
        if (type == 0) {
            //View 0: Hình ko caption
            /*
             * Hình ko caption, chỉ cần setImage và setOnClick cho imgPost, không cần set cho tvContent thứ khác (sẽ gây nullPointerException)
             */
            //  holder.imgPost.setImageResource(Integer.parseInt(post.getImgPost()));


            holder.imgPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ImageActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bd = new Bundle();
                    bd.putString("ImgRes", post.getImgPost());
                    bd.putString("ImgPoster", post.getName());
                    bd.putString("ImgUsername", post.getUsername());
                    bd.putString("ImgPfp", ava);
                    bd.putInt("idPost", post.getId());
                    i.putExtras(bd);
                    context.startActivity(i);
                }
            });
        } else if (type == 5) {

            holder.tvSharedOwner.setText("@" + childPost.getUsername());
            holder.tvTime.setText(childPost.getTime());
            holder.tvSharedCaption.setText(childPost.getContent());
            holder.tvSharedLikeCount.setText(childPost.getNumber_like());
            holder.ivSharedImage.setImageURI(Uri.parse(childPost.getImgPost()));
            holder.content.setText(childPost.getContent());
            //    holder.nameUserPost.setText(childPost.getUsername());

            holder.avatar.setImageURI(Uri.parse(post.getAvatar()));
            holder.name.setText(post.getName());
            holder.userName.setText(post.getUsername());
            holder.numberLike.setText(post.getNumber_like());
            holder.time.setText(post.getTime());

            holder.btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostDetailActitivty.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bn = new Bundle();
                    if (type == 0) {
                        bn.putString("Img", post.getImgPost());
                    } else if (type == 1 || type == 2) {
                        bn.putString("Content", post.getContent());
                    } else if (type == 3) {
                        bn.putString("Content", post.getContent());
                        bn.putString("Img", post.getImgPost());
                    }
                    bn.putString("Username", post.getUsername());
                    bn.putString("Pfp", post.getAvatar());
                    bn.putString("Name", post.getName());
                    bn.putBoolean("IsCmt", true);
                    bn.putInt("ViewType", type);
                    intent.putExtras(bn);
                    context.startActivity(intent);
                }
            });


        } else if (type == 1 || type == 2) {
            //View 1, View 2: Có caption nhưng ko có hình
            /*
             * Post chỉ có mỗi caption, nên chỉ setText cho mỗi caption, cố gắng setImage sẽ gây nullPointerException
             */
            holder.content.setText(post.getContent());
        } else if (type == 3) {
            //View 1, View 2: Có caption và hình
            /*
             * Post có cả 2 caption và hình, setImageResource và setText cho imgPost và content bình tường
             */
            holder.imgPost.setImageURI(Uri.parse(post.getImgPost()));
            Log.d("d", post.getImgPost());
            holder.content.setText(post.getContent());
            holder.imgPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ImageActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bd = new Bundle();
                    bd.putString("ImgRes", post.getImgPost());
                    bd.putString("ImgPoster", post.getName());
                    bd.putString("ImgUsername", post.getUsername());
                    bd.putString("ImgPfp", ava);
                    bd.putInt("idPost", post.getId());
                    i.putExtras(bd);
                    context.startActivity(i);
                }
            });
        }


        int idUserFollow = posts.get(position).getIduser();
        int idUser = user.getId();
        //Log.d("IDFollower: ", email);

        if (db.CheckNameinFollowing(idUserFollow, idUser)) {
            if (idUser >= 0) {
                holder.flo.setVisibility(View.GONE);
                holder.tvFollowed.setVisibility(View.VISIBLE);
            }
        } else {
            holder.flo.setVisibility(View.VISIBLE);
            holder.tvFollowed.setVisibility(View.GONE);
        }


        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDetailActitivty.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bn = new Bundle();
                if (type == 0) {
                    bn.putString("Img", post.getImgPost());
                } else if (type == 1 || type == 2) {
                    bn.putString("Content", post.getContent());
                } else if (type == 3) {
                    bn.putString("Content", post.getContent());
                    bn.putString("Img", post.getImgPost());
                }
                bn.putInt("idPost", post.getId());
                bn.putString("Username", post.getUsername());
                bn.putInt("idUser", post.getIduser());
                bn.putString("Pfp", ava);
                bn.putString("Name", post.getName());
                bn.putBoolean("IsCmt", true);
                bn.putInt("ViewType", type);
                bn.putInt("idUser", post.getIduser());
                bn.putString("Time", holder.time.getText().toString());
                bn.putString("Like", String.valueOf(db.getLike(post.getId()).getCount()));
                intent.putExtras(bn);
                context.startActivity(intent);
            }
        });


        int iduser = db.getIduser(name);
        int idpost = post.getId();

        if (!db.CheckLike(iduser, idpost)) {
            holder.btnLike.setBackgroundResource(R.drawable.favorite_svgrepo_com);
        } else {
            holder.btnLike.setBackgroundResource(R.drawable.outline_favorite_24);
        }

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.CheckLike(iduser, idpost) == false) {
                    Boolean insertLike = db.insertLikes(iduser, idpost);
                    if (insertLike == true && holder.btnLike.isChecked()) {
                        holder.btnLike.setChecked(false);
                        holder.btnLike.setBackgroundResource(R.drawable.outline_favorite_24);
                        holder.numberLike.setText(String.valueOf(db.getLike(idpost).getCount()));
                        notifyItemChanged(position);
                    }
                } else {
                    db.Unlike(iduser, idpost);
                    holder.btnLike.setBackgroundResource(R.drawable.favorite_svgrepo_com);
                    holder.numberLike.setText(String.valueOf(db.getLike(idpost).getCount()));
                    notifyItemChanged(position);
                }

            }

        });


        // menu


        if (idUserFollow != idUser) {
            if (idUser > 0)
                holder.btnOpenMenu.setVisibility(View.GONE);

        } else {
            holder.btnOpenMenu.setVisibility(View.VISIBLE);
            holder.flo.setVisibility(View.GONE);
            holder.tvFollowed.setVisibility(View.GONE);
        }

        holder.btnOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), v);
                refreshView(position);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit_post:
                                Intent i = new Intent(context, EditPostActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bundle bd = new Bundle();
                                bd.putInt("idPost", post.getId());
                                i.putExtras(bd);
                                context.startActivity(i);
                                break;
                            case R.id.remove_post:
                                AlertDialog.Builder b = new AlertDialog.Builder(v.getRootView().getContext());
                                b.setTitle("Xóa bài viết")
                                        .setMessage("Bạn có chắc là bạn muốn xóa bài viết này? Hành động này sẽ không thể đảo ngược.");
                                b.setPositiveButton("Ok, hãy xóa nó cho tôi.", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        db.removePost(post.getId());
                                        posts.remove(position);
                                        notifyItemRemoved(position);
                                    }
                                });
                                b.setNegativeButton("Hủy, đừng xóa nó", null);
                                AlertDialog a = b.create();
                                a.show();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.inflate(R.menu.menu_option_post);
                popupMenu.show();
            }
        });

        holder.flo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                visited = new Boolean[posts.size()];
                holder.flo.setVisibility(v.GONE);
                holder.tvFollowed.setVisibility(v.VISIBLE);

                String UserName = db.getName(user.getId());
                int idUserFollow = followUser(post.getUsername());
                String UserNameFollow = db.getName(idUserFollow);

//                if (!db.CheckNameinFollower(idUserFollow)) {
                db.insertDataFollow(idUser, idUserFollow);
                ArrayList<Integer> listUserFollowed = db.listIdUserOf(user.getId());
                int index;
                for (int i = 0; i < listUserFollowed.size(); i++) {
                    Log.d("IDFollower: ", String.valueOf(listUserFollowed.get(i)));
                    db = new DB(context);
                    if (idUserFollow == listUserFollowed.get(i)) {
                        holder.flo.setVisibility(View.GONE);
                        holder.tvFollowed.setVisibility(View.VISIBLE);
                    } else {
                        holder.flo.setVisibility(View.VISIBLE);
                        holder.tvFollowed.setVisibility(View.GONE);
                    }
                    //  refreshView(listUserFollowed.get(i));
                    for (int k = 0; k < 100; k++) {
                        refreshView(k);
                    }
                    for (int k = 100; k > 0; k--) {
                        refreshView(k);
                    }
                }
                Toast.makeText(context, "Followed", Toast.LENGTH_SHORT).show();
            }
        });

        holder.tvFollowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(v.getRootView().getContext());
                b.setTitle("Hủy theo dõi");
                b.setMessage("Bạn có muốn hủy theo dõi người dùng " + post.getName() + "? Bạn sẽ không thể thấy thông báo khi họ đăng bài.");
                b.setPositiveButton("Hủy theo dõi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        holder.flo.setVisibility(View.VISIBLE);
                        holder.tvFollowed.setVisibility(View.GONE);

                        user = db.getUser(email);
                        int idUser = user.getId();
                        String UserName = db.getName(user.getId());
                        int idUserFollow = followUser(post.getUsername());
                        String UserNameFollow = db.getName(idUserFollow);

                        if (db.CheckNameinFollower(idUserFollow)) {
                            if (idUser > 0) {
                                db.UnFollower(idUserFollow);
                                ArrayList<Integer> listUserFollowed = db.listIdUserOf(user.getId());

                                for (int j = 0; j < listUserFollowed.size(); j++) {
                                    Log.d("IDFollower: ", String.valueOf(listUserFollowed.get(j)));
                                    if (idUserFollow == listUserFollowed.get(j)) {
                                        holder.flo.setVisibility(View.GONE);
                                        holder.tvFollowed.setVisibility(View.VISIBLE);

                                    } else {
                                        holder.flo.setVisibility(View.VISIBLE);
                                        holder.tvFollowed.setVisibility(View.GONE);
                                    }
                                    //refreshView(j);
                                }
                                for (int k = 0; k < 100; k++) {
                                    refreshView(k);
                                }
                                for (int k = 100; k > 0; k--) {
                                    refreshView(k);
                                }

                                Toast.makeText(context, "UnFollowed", Toast.LENGTH_SHORT).show();


                            } else
                                Toast.makeText(context, "Bạn chưa có tài khoản .-.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Followed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                b.setNegativeButton("Hủy", null);
                AlertDialog a = b.create();
                a.show();
            }
        });


        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(context, SettingActivity.class);
            }
        });
        holder.likeWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, LikeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putInt("idPost", posts.get(position).getId());
                i.putExtras(b);
                context.startActivity(i);
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentIDpost = post.getId();
                int myID = idUser;
                Date currentTime = Calendar.getInstance().getTime();
                db.saveShare(myID, currentIDpost, String.valueOf(currentTime));
            }
        });

    }


    public int followUser(String name) {
        SQLiteDatabase mydb = db.getWritableDatabase();
        Cursor cursor = mydb.rawQuery("SELECT * FROM post p JOIN user u on u.id = p.iduser WHERE u.name = ?", new String[]{name});
        List<Integer> list = null;
        while (cursor.moveToNext()) {
            if (list == null)
                list = new ArrayList<>();
            list.add(cursor.getInt(1));
        }
        assert list != null;
        return list.get(0);
    }

    public ArrayList<Integer> ListFollowUserID(int idCurrentUser) {
        SQLiteDatabase mydb = db.getWritableDatabase();
        Cursor cursor = mydb.rawQuery("SELECT * FROM follower WHERE iduser = ?", new String[]{String.valueOf(idCurrentUser)});
        ArrayList<Integer> list = new ArrayList<Integer>();
        while (cursor.moveToNext()) {
            list.add(cursor.getInt(0));
        }

        return list;
    }

    public ArrayList<Integer> ListPostID() {
        SQLiteDatabase mydb = db.getWritableDatabase();
        Cursor cursor = mydb.rawQuery("SELECT * FROM post ", null);
        ArrayList<Integer> list = new ArrayList<Integer>();
        while (cursor.moveToNext()) {
            list.add(cursor.getInt(0));
        }

        return list;
    }


    @Override
    public int getItemCount() {
        if (posts != null)
            return posts.size();
        return 0;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        private ImageButton btnOpenMenu;

        private TextView tvFollowed;

        private Button flo;
        private CheckBox btnLike;
        private ImageButton btnComment, btnShare;
        private ShapeableImageView avatar;
        private ImageView imgPost, ivSharedImage;
        private TextView name, userName, numberLike, content, time, nameUserPost, tvSharedOwner, tvTime, tvSharedCaption, tvSharedLikeCount;
        private LinearLayout btnShowProfile, likeWrapper;

        LinearLayout llUser;

        public PostViewHolder(@NonNull View view) {
            super(view);
            likeWrapper = view.findViewById(R.id.likeWrapper);
            avatar = (ShapeableImageView) view.findViewById(R.id.avatar);
            imgPost = (ImageView) view.findViewById(R.id.img_post);
            name = (TextView) view.findViewById(R.id.name);
            userName = (TextView) view.findViewById(R.id.nameu_user);
            // nameUserPost = (TextView) view.findViewById(R.id.nameuser_post);
            numberLike = (TextView) view.findViewById(R.id.number_like);
            content = (TextView) view.findViewById(R.id.content_post);
            time = (TextView) view.findViewById(R.id.time_post);
            btnComment = (ImageButton) view.findViewById(R.id.btn_Pcomment);
            btnOpenMenu = (ImageButton) view.findViewById(R.id.btnOptions);
            btnLike = (CheckBox) view.findViewById(R.id.btn_like);
            tvFollowed = (TextView) view.findViewById(R.id.tvFollowed);
            flo = (Button) view.findViewById(R.id.btnFlolow);
            btnShowProfile = view.findViewById(R.id.btnShowProfile);
            // nameUserPost = view.findViewById(R.id.)
            //
            ivSharedImage = view.findViewById(R.id.ivSharedImage);
            tvSharedLikeCount = view.findViewById(R.id.tvSharedLikeCount);
            tvSharedOwner = view.findViewById(R.id.tvSharedOwner);
            tvTime = view.findViewById(R.id.tvTime);
            tvSharedCaption = view.findViewById(R.id.tvSharedCaption);
            tvSharedLikeCount = view.findViewById(R.id.tvSharedLikeCount);
            btnShare = view.findViewById(R.id.btn_Pshare);
        }


    }

    public void refreshView(int position) {
        notifyItemChanged(position);
    }

    Boolean[] visited;

    public int getIndexOfPost(int idFollower) {
        Arrays.fill(visited, false);
        int index = -1;
        for (int i = 0; i < getItemCount(); i++) {
            if (posts.get(i).getIduser() == idFollower) {
                if (visited[i]) {
                    continue;
                }
                Log.d("indexFollower", String.valueOf(i));
                visited[i] = true;
                return i;
            }
        }
        visited = new Boolean[posts.size()];
        return index;
    }
}
